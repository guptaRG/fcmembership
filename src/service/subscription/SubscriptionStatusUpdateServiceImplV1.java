package service.subscription;

import entity.SubscriptionEntity;
import entity.SubscriptionStatusUpdateEventEntity;
import exception.InvalidRequestException;
import model.enums.SubscriptionStatus;
import model.request.ActivateSubscriptionRequest;
import repository.inmemory.SubscriptionRepository;
import repository.inmemory.SubscriptionStatusUpdateEventRepository;
import util.DateUtil;

import java.util.Date;
import java.util.Objects;

public class SubscriptionStatusUpdateServiceImplV1 implements SubscriptionStatusUpdateService {
    private final SubscriptionStatusUpdateEventRepository subscriptionStatusUpdateEventRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPaymentService subscriptionPaymentService;

    public SubscriptionStatusUpdateServiceImplV1(
            SubscriptionStatusUpdateEventRepository subscriptionStatusUpdateEventRepository,
            SubscriptionRepository subscriptionRepository, SubscriptionPaymentService subscriptionPaymentService) {
        this.subscriptionStatusUpdateEventRepository = subscriptionStatusUpdateEventRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPaymentService = subscriptionPaymentService;
    }

    @Override
    public SubscriptionEntity activate(ActivateSubscriptionRequest activateSubscriptionRequest) {
        if (!activateSubscriptionRequest.isValid()) {
            throw  new InvalidRequestException("Invalid activate subscription request", null);
        }
        synchronized (activateSubscriptionRequest.subscriptionID()) {
            SubscriptionEntity subscription = subscriptionRepository.getById(activateSubscriptionRequest.subscriptionID())
                    .orElseThrow();
            if (Objects.isNull(activateSubscriptionRequest.paymentID()) &&
                    subscription.getPlanTier().getPlan().getPricePaise() +
                            subscription.getPlanTier().getAdditionalPaymentPaise() > 0) {
                throw new InvalidRequestException("Payment is required for this plan", null);
            }
            if (!subscription.getStatus().toStatusUpdateValid(SubscriptionStatus.ACTIVE)) {
                throw new  InvalidRequestException("Cannot activate the given subscription ID", null);
            }
            if (Objects.nonNull(activateSubscriptionRequest.paymentID())) {
                subscriptionPaymentService.addPayment(subscription, activateSubscriptionRequest.paymentID(),
                        subscription.getPlanTier().getPlan().getPricePaise() +
                                subscription.getPlanTier().getAdditionalPaymentPaise());
            }
            Date currentTermStart = new Date();
            subscription = subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setCurrentTermStart(currentTermStart);
            subscription.setCurrentTermEnd(DateUtil.addDuration(currentTermStart,
                    subscription.getPlanTier().getPlan().getDuration()));
            subscription = subscriptionRepository.update(subscription);
            subscriptionStatusUpdateEventRepository.save(new SubscriptionStatusUpdateEventEntity(
                    subscription.getStatus(), subscription, currentTermStart));
            return subscription;
        }
    }
}
