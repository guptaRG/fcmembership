package service.subscription;

import entity.SubscriptionEntity;
import entity.SubscriptionPaymentsEntity;
import repository.inmemory.SubscriptionPaymentsRepository;

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
}
