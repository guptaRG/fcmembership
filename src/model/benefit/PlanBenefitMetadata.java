package model.benefit;

public class PlanBenefitMetadata {
    private FreeDeliveryBenefitMetadata freeDeliveryMetadata;
    private PercentDiscountBenefitMetadata percentDiscountMetadata;
    private ExclusiveDealsBenefitMetadata exclusiveDealsMetadata;
    private SalesEarlyAccessBenefitMetadata salesEarlyAccessMetadata;
    private PriorityCSBenefitMetadata priorityCSMetadata;
    private ExclusiveCouponsBenefitMetadata exclusiveCouponsMetadata;
    private FastDeliveryBenefitMetadata fastDeliveryMetadata;

    public PlanBenefitMetadata(FreeDeliveryBenefitMetadata freeDeliveryMetadata) {
        this.freeDeliveryMetadata = freeDeliveryMetadata;
    }

    public PlanBenefitMetadata(PercentDiscountBenefitMetadata percentDiscountMetadata) {
        this.percentDiscountMetadata = percentDiscountMetadata;
    }

    public PlanBenefitMetadata(ExclusiveDealsBenefitMetadata exclusiveDealsMetadata) {
        this.exclusiveDealsMetadata = exclusiveDealsMetadata;
    }

    public PlanBenefitMetadata(SalesEarlyAccessBenefitMetadata salesEarlyAccessMetadata) {
        this.salesEarlyAccessMetadata = salesEarlyAccessMetadata;
    }

    public PlanBenefitMetadata(PriorityCSBenefitMetadata priorityCSMetadata) {
        this.priorityCSMetadata = priorityCSMetadata;
    }

    public PlanBenefitMetadata(FastDeliveryBenefitMetadata fastDeliveryMetadata) {
        this.fastDeliveryMetadata = fastDeliveryMetadata;
    }

    public PlanBenefitMetadata(ExclusiveCouponsBenefitMetadata exclusiveCouponsMetadata) {
        this.exclusiveCouponsMetadata = exclusiveCouponsMetadata;
    }

    public FreeDeliveryBenefitMetadata getFreeDeliveryMetadata() {
        return freeDeliveryMetadata;
    }

    public void setFreeDeliveryMetadata(FreeDeliveryBenefitMetadata freeDeliveryMetadata) {
        this.freeDeliveryMetadata = freeDeliveryMetadata;
    }

    public PercentDiscountBenefitMetadata getPercentDiscountMetadata() {
        return percentDiscountMetadata;
    }

    public void setPercentDiscountMetadata(PercentDiscountBenefitMetadata percentDiscountMetadata) {
        this.percentDiscountMetadata = percentDiscountMetadata;
    }

    public ExclusiveDealsBenefitMetadata getExclusiveDealsMetadata() {
        return exclusiveDealsMetadata;
    }

    public void setExclusiveDealsMetadata(ExclusiveDealsBenefitMetadata exclusiveDealsMetadata) {
        this.exclusiveDealsMetadata = exclusiveDealsMetadata;
    }

    public SalesEarlyAccessBenefitMetadata getSalesEarlyAccessMetadata() {
        return salesEarlyAccessMetadata;
    }

    public void setSalesEarlyAccessMetadata(SalesEarlyAccessBenefitMetadata salesEarlyAccessMetadata) {
        this.salesEarlyAccessMetadata = salesEarlyAccessMetadata;
    }

    public PriorityCSBenefitMetadata getPriorityCSMetadata() {
        return priorityCSMetadata;
    }

    public void setPriorityCSMetadata(PriorityCSBenefitMetadata priorityCSMetadata) {
        this.priorityCSMetadata = priorityCSMetadata;
    }

    public ExclusiveCouponsBenefitMetadata getExclusiveCouponsMetadata() {
        return exclusiveCouponsMetadata;
    }

    public void setExclusiveCouponsMetadata(ExclusiveCouponsBenefitMetadata exclusiveCouponsMetadata) {
        this.exclusiveCouponsMetadata = exclusiveCouponsMetadata;
    }

    public FastDeliveryBenefitMetadata getFastDeliveryMetadata() {
        return fastDeliveryMetadata;
    }

    public void setFastDeliveryMetadata(FastDeliveryBenefitMetadata fastDeliveryMetadata) {
        this.fastDeliveryMetadata = fastDeliveryMetadata;
    }
}
