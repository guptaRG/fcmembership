package service.subscription;

import entity.SubscriptionEntity;
import entity.SubscriptionPaymentsEntity;

import java.util.Date;

public interface SubscriptionPaymentService {
    SubscriptionPaymentsEntity addPayment(SubscriptionEntity subscription, String paymentId, int amtPaidPaise);
    SubscriptionPaymentsEntity delete(SubscriptionPaymentsEntity entity);
    void handlePausedSubscriptionCancellationRefund(SubscriptionEntity subs, Date cancellationDate);
}
