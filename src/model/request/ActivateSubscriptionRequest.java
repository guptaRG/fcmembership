package model.request;

import util.StringUtil;

public record ActivateSubscriptionRequest(String subscriptionID, String paymentID) {

    public boolean isValid() {
        return !StringUtil.isEmpty(subscriptionID);
    }
}
