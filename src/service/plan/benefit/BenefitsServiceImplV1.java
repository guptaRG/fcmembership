package service.plan.benefit;

import entity.PercentDiscountBenefitItemsEntity;
import entity.PlanBenefitEntity;
import exception.InvalidRequestException;
import model.enums.MembershipBenefit;
import repository.inmemory.PercentDiscountBenefitItemsRepository;
import repository.inmemory.PlanBenefitRepository;

import java.util.List;

public class BenefitsServiceImplV1 implements BenefitsService {
    private final PlanBenefitRepository planBenefitRepository;
    private final PercentDiscountBenefitItemsRepository percentDiscountBenefitItemsRepository;

    public BenefitsServiceImplV1(PlanBenefitRepository planBenefitRepository,
                                 PercentDiscountBenefitItemsRepository percentDiscountBenefitItemsRepository) {
        this.planBenefitRepository = planBenefitRepository;
        this.percentDiscountBenefitItemsRepository = percentDiscountBenefitItemsRepository;
    }

    @Override
    public List<PercentDiscountBenefitItemsEntity> addPercentDiscountBenefitItem(String planBenefitID, String itemID) {
        PlanBenefitEntity planBenefit = planBenefitRepository.getById(planBenefitID).orElseThrow();
        if (planBenefit.getBenefit() != MembershipBenefit.PERCENT_DISCOUNT) {
            throw new InvalidRequestException("Provided planBenefitID is not a PERCENT_DISCOUNT benefit ID", null);
        }
        percentDiscountBenefitItemsRepository.save(new PercentDiscountBenefitItemsEntity(planBenefit, itemID));
        return percentDiscountBenefitItemsRepository.getAllByBenefit(planBenefit);
    }
}
