package model.request;

public record CreateSubscriptionRequest(String userID, String planTierID, String planID) {
}
