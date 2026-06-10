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

    @Override
    public SubscriptionEntity cancel(SubscriptionEntity subscription, String userID) {
        if (!subscription.getUserID().equals(userID)) {
            throw new InvalidRequestException("Not authorized to cancel this subscription", null);
        }
        if (!subscription.getStatus().toStatusUpdateValid(SubscriptionStatus.CANCELED)) {
            throw new InvalidRequestException("Invalid status update", null);
        }
        Date cancellationDate = new Date();
        if (cancellationDate.after(subscription.getCurrentTermEnd())) {
            throw new InvalidRequestException("The subscription is already expired", null);
        }
        if (cancellationDate.equals(subscription.getCurrentTermEnd()) ||
                subscription.getStatus() == SubscriptionStatus.PAUSED) {

            subscription.setStatus(SubscriptionStatus.CANCELED);
            subscription = subscriptionRepository.update(subscription);
            if (subscription.getStatus() == SubscriptionStatus.PAUSED) {
                subscriptionPaymentService.handlePausedSubscriptionCancellationRefund(subscription, cancellationDate);
            }
        }
        subscriptionStatusUpdateEventRepository.save(new SubscriptionStatusUpdateEventEntity(
                SubscriptionStatus.CANCELED, subscription, cancellationDate));
        return subscription;
    }
}
