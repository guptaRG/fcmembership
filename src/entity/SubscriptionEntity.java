package entity;

import model.enums.SubscriptionStatus;

import java.util.Date;

public class SubscriptionEntity extends BaseEntity {
    private final String userID;
    private final MembershipPlanEntity plan;
    private Date currentTermStart;
    private Date currentTermEnd;
    private SubscriptionStatus status;
    private String paymentID;

    public SubscriptionEntity(String userID, MembershipPlanEntity plan) {
        super();
        this.userID = userID;
        this.plan = plan;
        this.status = SubscriptionStatus.NEW_LEAD;
    }

    public String getUserID() {
        return userID;
    }

    public MembershipPlanEntity getPlan() {
        return plan;
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

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }
}
