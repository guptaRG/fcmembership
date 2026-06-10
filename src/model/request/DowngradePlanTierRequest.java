package model.request;

import entity.PlanTierEntity;
import util.StringUtil;

public record DowngradePlanTierRequest(PlanTierEntity newPlanTier, String subscriptionID, String userID) {

    public boolean isValid() {
        return !StringUtil.isEmpty(subscriptionID) && !StringUtil.isEmpty(userID);
    }
}
