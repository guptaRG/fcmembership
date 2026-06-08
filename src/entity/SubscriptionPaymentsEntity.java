package entity;

public class SubscriptionPaymentsEntity extends BaseEntity {
    private final SubscriptionEntity subscription;
    private final String paymentID;
    private final PlanTierEntity planTier;
    private final int amountPaidPaise;

    public SubscriptionPaymentsEntity(SubscriptionEntity subscription, String paymentID, PlanTierEntity planTier, int amountPaidPaise) {
        this.subscription = subscription;
        this.paymentID = paymentID;
        this.planTier = planTier;
        this.amountPaidPaise = amountPaidPaise;
    }

    public SubscriptionEntity getSubscription() {
        return subscription;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public PlanTierEntity getPlanTier() {
        return planTier;
    }

    public int getAmountPaidPaise() {
        return amountPaidPaise;
    }
}
