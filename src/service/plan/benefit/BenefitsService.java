package service.plan.benefit;

import entity.PercentDiscountBenefitItemsEntity;

import java.util.List;

public interface BenefitsService {
    List<PercentDiscountBenefitItemsEntity> addPercentDiscountBenefitItem(String planBenefitID, String itemID);
}
