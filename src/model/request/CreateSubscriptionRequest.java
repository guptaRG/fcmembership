package model.request;

import util.StringUtil;

public record CreateSubscriptionRequest(String userID, String planTierID, String planID) {

    public boolean isValid() {
        return !StringUtil.isEmpty(userID) &&
                // Both plan ID and plan tier ID can't be empty
                (StringUtil.isEmpty(planTierID) || StringUtil.isEmpty(planID)) &&
                !(StringUtil.isEmpty(planTierID) && StringUtil.isEmpty(planID));
    }
}
