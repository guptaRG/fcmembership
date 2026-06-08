package entity;

import model.benefit.PlanBenefitMetadata;
import model.enums.MembershipBenefit;

public class PlanBenefitEntity extends BaseEntity {
    private final MembershipBenefit benefit;
    private final PlanTierEntity planTier;
    private PlanBenefitMetadata metadata;

    public PlanBenefitEntity(MembershipBenefit benefit, PlanTierEntity planTier, PlanBenefitMetadata metadata) {
        super();
        this.benefit = benefit;
        this.planTier = planTier;
        this.metadata = metadata;
    }

    public MembershipBenefit getBenefit() {
        return benefit;
    }

    public PlanTierEntity getPlanTier() {
        return planTier;
    }

    public PlanBenefitMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PlanBenefitMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "PlanBenefitEntity{" +
                "benefit=" + benefit +
                ", metadata=" + metadata +
                '}';
    }
}
