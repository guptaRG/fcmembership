package service.subscription;

import entity.SubscriptionEntity;
import entity.SubscriptionPaymentsEntity;
import exception.InvalidRequestException;
import repository.inmemory.SubscriptionPaymentsRepository;
import util.StringUtil;

public class SubscriptionPaymentServiceImplV1 implements SubscriptionPaymentService {
    private final SubscriptionPaymentsRepository subscriptionPaymentsRepository;

    public SubscriptionPaymentServiceImplV1(SubscriptionPaymentsRepository subscriptionPaymentsRepository) {
        this.subscriptionPaymentsRepository = subscriptionPaymentsRepository;
    }

    @Override
    public SubscriptionPaymentsEntity addPayment(SubscriptionEntity subscription, String paymentID, int amtPaidPaise) {
        if (StringUtil.isEmpty(paymentID)) {
            throw new InvalidRequestException("paymentID is null", null);
        }
        // Add checks to confirm payment status from payments service
        return subscriptionPaymentsRepository.save(new SubscriptionPaymentsEntity(subscription, paymentID,
                subscription.getPlanTier(), amtPaidPaise));
    }
}
