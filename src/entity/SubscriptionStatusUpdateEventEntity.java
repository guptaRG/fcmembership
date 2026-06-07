package entity;

import model.enums.SubscriptionStatus;

import java.util.Date;
import java.util.Set;

public class SubscriptionStatusUpdateEventEntity extends BaseEntity {
    private final SubscriptionStatus subscriptionStatus;
    private final SubscriptionEntity subscriptionEntity;
    private final Date eventTime;

    public SubscriptionStatusUpdateEventEntity(SubscriptionStatus subscriptionStatus,
                                               SubscriptionEntity subscriptionEntity, Date eventTime) {
        super();
        this.subscriptionStatus = subscriptionStatus;
        this.subscriptionEntity = subscriptionEntity;
        this.eventTime = eventTime;
    }

    public SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public SubscriptionEntity getSubscriptionEntity() {
        return subscriptionEntity;
    }

    public Date getEventTime() {
        return eventTime;
    }
}
