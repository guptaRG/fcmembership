package service.subscription;

import entity.SubscriptionEntity;
import entity.SubscriptionPaymentsEntity;

public interface SubscriptionPaymentService {
    SubscriptionPaymentsEntity addPayment(SubscriptionEntity subscription, String paymentId, int amtPaidPaise);
}
