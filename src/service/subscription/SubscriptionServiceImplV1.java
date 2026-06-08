package service.subscription;

import constants.IntConstants;
import entity.MembershipPlanEntity;
import entity.PlanTierEntity;
import entity.SubscriptionEntity;
import exception.InvalidRequestException;
import model.request.CreateSubscriptionRequest;
import repository.inmemory.MembershipPlanRepository;
import repository.inmemory.PlanTierRepository;
import repository.inmemory.SubscriptionRepository;
import repository.inmemory.SubscriptionStatusUpdateEventRepository;
import util.StringUtil;

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

        SubscriptionEntity subscription = subscriptionRepository.save(new SubscriptionEntity())
    }
}
