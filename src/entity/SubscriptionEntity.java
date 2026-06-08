package entity;

import model.enums.SubscriptionStatus;

import java.util.Date;

public class SubscriptionEntity extends BaseEntity implements Cloneable {
    private final String userID;
    private PlanTierEntity planTier;
    private Date currentTermStart;
    private Date currentTermEnd;
    private SubscriptionStatus status;
    private String paymentID;
    private boolean automaticTierUpgradeEnabled;

    public SubscriptionEntity(String userID, PlanTierEntity planTier) {
        super();
        this.userID = userID;
        this.planTier = planTier;
        this.status = SubscriptionStatus.NEW_LEAD;
        this.automaticTierUpgradeEnabled = true;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isAutomaticTierUpgradeEnabled() {
        return automaticTierUpgradeEnabled;
    }

    public void setAutomaticTierUpgradeEnabled(boolean automaticTierUpgradeEnabled) {
        this.automaticTierUpgradeEnabled = automaticTierUpgradeEnabled;
    }

    public PlanTierEntity getPlanTier() {
        return planTier;
    }

    public Date getCurrentTermStart() {
        return currentTermStart;
    }

    public void setCurrentTermStart(Date currentTermStart) {
        this.currentTermStart = currentTermStart;
    }

    public Date getCurrentTermEnd() {
        return currentTermEnd;
    }

    public void setCurrentTermEnd(Date currentTermEnd) {
        this.currentTermEnd = currentTermEnd;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public SubscriptionEntity setStatus(SubscriptionStatus status) {
        SubscriptionEntity newSubscriptionEntity = this.clone();
        newSubscriptionEntity.status = status;
        return newSubscriptionEntity;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public void setPlanTier(PlanTierEntity planTier) {
        this.planTier = planTier;
    }

    @Override
    public SubscriptionEntity clone() {
        try {
            SubscriptionEntity clone = (SubscriptionEntity) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
