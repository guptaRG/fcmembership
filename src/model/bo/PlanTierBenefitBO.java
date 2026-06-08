package model.bo;

import model.benefit.PlanBenefitMetadata;
import model.enums.MembershipBenefit;

public record PlanTierBenefitBO(MembershipBenefit benefit, PlanBenefitMetadata metadata, String id) {
}
