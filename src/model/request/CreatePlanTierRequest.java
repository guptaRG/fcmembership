package model.request;

import java.util.List;

public record CreatePlanTierRequest(int tier, String tierName, String description,
                                    List<CreatePlanTierBenefitRequest> benefits, int additionalPaymentPaise) {
}
