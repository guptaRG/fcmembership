package service.subscription;

import constants.IntConstants;
import entity.*;
import exception.InconsistentDBStateException;
import exception.InvalidRequestException;
import model.enums.SubscriptionStatus;
import model.request.CreateSubscriptionRequest;
import model.request.DowngradePlanTierRequest;
import model.request.UpgradePlanTierRequest;
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
    private final SubscriptionPaymentService subscriptionPaymentService;

    public SubscriptionServiceImplV1(SubscriptionRepository subscriptionRepository,
                                     SubscriptionStatusUpdateEventRepository subscriptionStatusUpdateEventRepository,
                                     MembershipPlanRepository membershipPlanRepository,
                                     PlanTierRepository planTierRepository,
                                     SubscriptionPaymentService subscriptionPaymentService) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionStatusUpdateEventRepository = subscriptionStatusUpdateEventRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.planTierRepository = planTierRepository;
        this.subscriptionPaymentService = subscriptionPaymentService;
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

    @Override
    public SubscriptionEntity upgradePlanTier(UpgradePlanTierRequest upgradePlanTierRequest) {
        if (!upgradePlanTierRequest.isValid()) {
            throw new InvalidRequestException("Invalid upgrade plan tier request", null);
        }
        SubscriptionEntity subs = subscriptionRepository.getById(upgradePlanTierRequest.subscriptionID()).orElseThrow();
        if (!subs.getUserID().equals(upgradePlanTierRequest.userID())) {
            throw new InvalidRequestException("Not authorized to upgrade plan tier", null);
        }
        if (subs.getStatus() == SubscriptionStatus.CANCELED ||
                subs.getStatus() == SubscriptionStatus.NEW_LEAD ||
                subs.getCurrentTermEnd().compareTo(new Date()) <= 0) {

            throw new InvalidRequestException("Subscription needs to be active/paused for upgrading the plan tier",
                    null);
        }
        if (subs.getPlanTier().getTier() >= upgradePlanTierRequest.newPlanTier().getTier()) {
            throw new InvalidRequestException("Can only upgrade the plan to a higher tier", null);
        }
        subs.setPlanTier(upgradePlanTierRequest.newPlanTier());
        subs.setAutomaticTierChangeEnabled(false);
        synchronized (subs.getId()) {
            SubscriptionPaymentsEntity paymentsEntity = subscriptionPaymentService.addPayment(subs,
                    upgradePlanTierRequest.paymentID(), upgradePlanTierRequest.newPlanTier().getAdditionalPaymentPaise() -
                            subs.getPlanTier().getAdditionalPaymentPaise());
            try {
                return subscriptionRepository.update(subs);
            } catch (Exception e) {
                subscriptionPaymentService.delete(paymentsEntity);
                throw e;
            }
        }
    }

    @Override
    public SubscriptionEntity downgradePlanTier(DowngradePlanTierRequest downgradePlanTierRequest) {
        if (!downgradePlanTierRequest.isValid()) {
            throw new InvalidRequestException("Invalid downgrade plan tier request", null);
        }
        SubscriptionEntity subs = subscriptionRepository.getById(downgradePlanTierRequest.subscriptionID())
                .orElseThrow();
        if (!subs.getUserID().equals(downgradePlanTierRequest.userID())) {
            throw new InvalidRequestException("Not authorized to downgrade plan tier", null);
        }
        if (subs.getStatus() == SubscriptionStatus.CANCELED ||
                subs.getStatus() == SubscriptionStatus.NEW_LEAD ||
                subs.getCurrentTermEnd().compareTo(new Date()) <= 0) {

            throw new InvalidRequestException("Subscription needs to be active/paused for downgrading the plan tier",
                    null);
        }
        if (subs.getPlanTier().getTier() <= downgradePlanTierRequest.newPlanTier().getTier()) {
            throw new InvalidRequestException("Can only downgrade the plan to a lower tier", null);
        }
        subs.setPlanTier(downgradePlanTierRequest.newPlanTier());
        subs.setAutomaticTierChangeEnabled(false);
        synchronized (subs.getId()) {
            SubscriptionPaymentsEntity paymentsEntity = subscriptionPaymentService.addPayment(subs, null,
                    downgradePlanTierRequest.newPlanTier().getAdditionalPaymentPaise() -
                            subs.getPlanTier().getAdditionalPaymentPaise());
            try {
                return subscriptionRepository.update(subs);
            } catch (Exception e) {
                subscriptionPaymentService.delete(paymentsEntity);
                throw e;
            }
        }
    }

    @Override
    public SubscriptionEntity getCurrent(String userID) {
        List<SubscriptionEntity> activeSubscriptions = subscriptionRepository.getAllByUserSubscriptionStatus(userID,
                SubscriptionStatus.ACTIVE);
        if (activeSubscriptions.size() > 1) {
            throw new InconsistentDBStateException("Active subscriptions can't be more than 1 for the same user", null);
        }
        if (!activeSubscriptions.isEmpty()) {
            return activeSubscriptions.get(0);
        }
        List<SubscriptionEntity> newLeadSubscriptions = subscriptionRepository.getAllByUserSubscriptionStatus(userID,
                SubscriptionStatus.NEW_LEAD);
        if (newLeadSubscriptions.size() > 1) {
            throw new InconsistentDBStateException("Lead subscriptions can't be more than 1 for the same user", null);
        }
        return newLeadSubscriptions.get(0);
    }
}
