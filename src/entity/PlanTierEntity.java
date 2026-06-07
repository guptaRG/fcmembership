package entity;

public class PlanTierEntity extends BaseEntity {
    private final MembershipPlanEntity plan;
    private final int tier;
    private String tierName;
    private String tierDescription;

    public PlanTierEntity(MembershipPlanEntity plan, int tier, String tierName, String tierDescription) {
        super();
        this.plan = plan;
        this.tier = tier;
        this.tierName = tierName;
        this.tierDescription = tierDescription;
    }

    public MembershipPlanEntity getPlan() {
        return plan;
    }

    public int getTier() {
        return tier;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public String getTierDescription() {
        return tierDescription;
    }

    public void setTierDescription(String tierDescription) {
        this.tierDescription = tierDescription;
    }
}
