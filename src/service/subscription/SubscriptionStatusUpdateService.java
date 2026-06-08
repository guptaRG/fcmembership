package service.subscription;

import entity.SubscriptionEntity;
import model.request.ActivateSubscriptionRequest;

public interface SubscriptionStatusUpdateService {
    SubscriptionEntity activate(ActivateSubscriptionRequest activateSubscriptionRequest);
}
