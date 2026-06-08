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

public class SubscriptionStatusUpdateServiceImplV1 implements SubscriptionStatusUpdateService {
    private final SubscriptionStatusUpdateEventRepository subscriptionStatusUpdateEventRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionStatusUpdateServiceImplV1(
            SubscriptionStatusUpdateEventRepository subscriptionStatusUpdateEventRepository,
            SubscriptionRepository subscriptionRepository) {
        this.subscriptionStatusUpdateEventRepository = subscriptionStatusUpdateEventRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public SubscriptionEntity activate(ActivateSubscriptionRequest activateSubscriptionRequest) {
        if (!activateSubscriptionRequest.isValid()) {
            throw  new InvalidRequestException("Invalid activate subscription request", null);
        }
        synchronized (activateSubscriptionRequest.subscriptionID()) {
            SubscriptionEntity subscription = subscriptionRepository.getById(activateSubscriptionRequest.subscriptionID())
                    .orElseThrow();
            if (!subscription.getStatus().toStatusUpdateValid(SubscriptionStatus.ACTIVE)) {
                throw new  InvalidRequestException("Cannot activate the given subscription ID", null);
            }
            Date currentTermStart = new Date();
            subscription = subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setCurrentTermStart(currentTermStart);
            subscription.setCurrentTermEnd(DateUtil.addDuration(currentTermStart,
                    subscription.getPlanTier().getPlan().getDuration()));
            subscription.setPaymentID(activateSubscriptionRequest.paymentID());
            subscription = subscriptionRepository.update(subscription);
            subscriptionStatusUpdateEventRepository.save(new SubscriptionStatusUpdateEventEntity(
                    subscription.getStatus(), subscription, currentTermStart));
            return subscription;
        }
    }
}
