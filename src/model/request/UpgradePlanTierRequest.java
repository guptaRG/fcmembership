package model.request;

import entity.PlanTierEntity;
import util.StringUtil;

public record UpgradePlanTierRequest(PlanTierEntity newPlanTier, String subscriptionID, String userID,
                                     String paymentID) {

    public boolean isValid() {
        return !StringUtil.isEmpty(subscriptionID) && !StringUtil.isEmpty(userID) && !StringUtil.isEmpty(paymentID);
    }
}
