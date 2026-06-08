package service.subscription;

import entity.SubscriptionEntity;
import model.request.CreateSubscriptionRequest;

public interface SubscriptionService {
    SubscriptionEntity create(CreateSubscriptionRequest createSubscriptionRequest);
}
