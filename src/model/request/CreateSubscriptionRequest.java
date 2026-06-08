package model.request;

import util.StringUtil;

public record CreateSubscriptionRequest(String userID, String planTierID, String planID) {

    public boolean isValid() {
        return !StringUtil.isEmpty(userID) && (StringUtil.isEmpty(planTierID) || StringUtil.isEmpty(planID));
    }
}
