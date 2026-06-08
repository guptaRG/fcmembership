package model.request;

import model.benefit.PlanBenefitMetadata;
import model.enums.MembershipBenefit;

public record CreatePlanTierBenefitRequest(MembershipBenefit benefit, PlanBenefitMetadata metadata) {
}
