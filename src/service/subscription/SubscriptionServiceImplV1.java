package service.subscription;

import constants.IntConstants;
import entity.MembershipPlanEntity;
import entity.PlanTierEntity;
import entity.SubscriptionEntity;
import entity.SubscriptionStatusUpdateEventEntity;
import exception.InconsistentDBStateException;
import exception.InvalidRequestException;
import model.enums.SubscriptionStatus;
import model.request.CreateSubscriptionRequest;
import repository.inmemory.MembershipPlanRepository;
import repository.inmemory.PlanTierRepository;
import repository.inmemory.SubscriptionRepository;
import repository.inmemory.SubscriptionStatusUpdateEventRepository;
import util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SubscriptionServiceImplV1 implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionStatusUpdateEventRepository subscriptionStatusUpdateEventRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final PlanTierRepository planTierRepository;

    public SubscriptionServiceImplV1(SubscriptionRepository subscriptionRepository,
                                     SubscriptionStatusUpdateEventRepository subscriptionStatusUpdateEventRepository,
                                     MembershipPlanRepository membershipPlanRepository, PlanTierRepository planTierRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionStatusUpdateEventRepository = subscriptionStatusUpdateEventRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.planTierRepository = planTierRepository;
    }

    @Override
    public SubscriptionEntity create(CreateSubscriptionRequest createSubscriptionRequest) {
        if (Objects.isNull(createSubscriptionRequest) || !createSubscriptionRequest.isValid()) {
            throw new InvalidRequestException("Invalid create subscription request", null);
        }
        PlanTierEntity planTier;
        if (StringUtil.isEmpty(createSubscriptionRequest.planTierID())) {
            MembershipPlanEntity plan = membershipPlanRepository.getById(createSubscriptionRequest.planID())
                    .orElseThrow();
            planTier = planTierRepository.getByPlanTier(plan, IntConstants.DEFAULT_PLAN_TIER).orElseThrow();
        } else {
            planTier = planTierRepository.getById(createSubscriptionRequest.planTierID()).orElseThrow();
        }
        synchronized (createSubscriptionRequest.userID()) {
            synchronized (planTier.getPlan().getId()) {
                List<SubscriptionEntity> newLeadMatchingEntities =
                        subscriptionRepository.getAllByUserSubscriptionStatus(createSubscriptionRequest.userID(),
                                        SubscriptionStatus.NEW_LEAD)
                                .stream()
                                .filter(subscription -> subscription.getPlanTier().getPlan() == planTier.getPlan())
                                .toList();
                if (newLeadMatchingEntities.size() > 1) {
                    throw new InconsistentDBStateException(
                            "Number of new lead subscriptions of a user for the same plan can't more than 1.", null);
                }
                if (newLeadMatchingEntities.isEmpty()) {
                    SubscriptionEntity subscription = subscriptionRepository.save(new SubscriptionEntity(
                            createSubscriptionRequest.userID(), planTier));
                    subscriptionStatusUpdateEventRepository.save(new SubscriptionStatusUpdateEventEntity(
                            subscription.getStatus(), subscription, new Date()));
                    return subscription;
                }
                newLeadMatchingEntities.get(0).setPlanTier(planTier);
                return subscriptionRepository.update(newLeadMatchingEntities.get(0));
            }
        }
    }
}
