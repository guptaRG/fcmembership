package model.benefit;

import java.util.List;

public record PercentDiscountBenefitMetadata(int discountBPS, List<String> eligibleCategoryIDs, List<String> eligibleItemIDs) {
}
