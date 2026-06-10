package service.subscription;

import entity.SubscriptionEntity;
import model.request.CreateSubscriptionRequest;
import model.request.DowngradePlanTierRequest;
import model.request.UpgradePlanTierRequest;

public interface SubscriptionService {
    SubscriptionEntity create(CreateSubscriptionRequest createSubscriptionRequest);
    SubscriptionEntity upgradePlanTier(UpgradePlanTierRequest upgradePlanTierRequest);
    SubscriptionEntity downgradePlanTier(DowngradePlanTierRequest downgradePlanTierRequest);
}
