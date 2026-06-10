package service.subscription;

import entity.SubscriptionEntity;
import entity.SubscriptionPaymentsEntity;
import exception.EntityNotFoundException;
import repository.inmemory.SubscriptionPaymentsRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SubscriptionPaymentServiceImplV1 implements SubscriptionPaymentService {
    private final SubscriptionPaymentsRepository subscriptionPaymentsRepository;

    public SubscriptionPaymentServiceImplV1(SubscriptionPaymentsRepository subscriptionPaymentsRepository) {
        this.subscriptionPaymentsRepository = subscriptionPaymentsRepository;
    }

    @Override
    public SubscriptionPaymentsEntity addPayment(SubscriptionEntity subscription, String paymentID, int amtPaidPaise) {
        // Add checks to confirm payment status from payments service
        return subscriptionPaymentsRepository.save(new SubscriptionPaymentsEntity(subscription, paymentID,
                subscription.getPlanTier(), amtPaidPaise));
    }

    @Override
    public SubscriptionPaymentsEntity delete(SubscriptionPaymentsEntity entity) {
        return subscriptionPaymentsRepository.delete(entity);
    }

    @Override
    public void handlePausedSubscriptionCancellationRefund(SubscriptionEntity subs,
                                                                          Date cancellationDate) {
        int currentTermTotalPaidPaise = getCurrentTermTotalPaymentPaise(subs);
        long leftOverDays = TimeUnit.MILLISECONDS.toDays(subs.getCurrentTermEnd().getTime() -
                cancellationDate.getTime());
        int refundAmtPaise = Math.toIntExact(currentTermTotalPaidPaise * leftOverDays /
                subs.getPlanTier().getPlan().getDuration().toDays());
        // Process refund to store payment ID
        subscriptionPaymentsRepository.save(new SubscriptionPaymentsEntity(subs, null, subs.getPlanTier(),
                -refundAmtPaise));
    }

    private int getCurrentTermTotalPaymentPaise(SubscriptionEntity subscription) {
        List<SubscriptionPaymentsEntity> currentTermPayments =
                subscriptionPaymentsRepository.getAllBySubscription(subscription)
                        .stream()
                        .filter(subscriptionPayment ->
                                subscriptionPayment.getCreatedAt().compareTo(subscription.getCurrentTermStart()) >= 0)
                        .toList();
        if (currentTermPayments.isEmpty()) {
            throw new EntityNotFoundException(SubscriptionPaymentsEntity.class, null);
        }
        return currentTermPayments.stream().mapToInt(SubscriptionPaymentsEntity::getAmountPaidPaise).sum();
    }
}
