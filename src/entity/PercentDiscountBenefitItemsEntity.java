package entity;

public class PercentDiscountBenefitItemsEntity extends BaseEntity {
    private final PlanBenefitEntity benefit;
    private final String itemID;

    public PercentDiscountBenefitItemsEntity(PlanBenefitEntity benefit, String itemID) {
        super();
        this.benefit = benefit;
        this.itemID = itemID;
    }

    public PlanBenefitEntity getBenefit() {
        return benefit;
    }

    public String getItemID() {
        return itemID;
    }
}
