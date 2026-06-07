package model.benefit;

public record PlanBenefitMetadata(FreeDeliveryBenefitMetadata freeDeliveryMetadata,
                                  PercentDiscountBenefitMetadata percentDiscountMetadata,
                                  ExclusiveDealsBenefitMetadata exclusiveDealsMetadata,
                                  SalesEarlyAccessBenefitMetadata salesEarlyAccessMetadata,
                                  PriorityCSBenefitMetadata priorityCSMetadata,
                                  ExclusiveCouponsBenefitMetadata exclusiveCouponsMetadata,
                                  FastDeliveryBenefitMetadata fastDeliveryMetadata) {}
