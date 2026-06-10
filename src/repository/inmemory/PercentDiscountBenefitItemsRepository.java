package repository.inmemory;

import entity.PercentDiscountBenefitItemsEntity;
import entity.PlanBenefitEntity;
import exception.DuplicateEntriesException;
import exception.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PercentDiscountBenefitItemsRepository extends
        AbstractInMemoryRepository<PercentDiscountBenefitItemsEntity> {
    private final Map<PlanBenefitEntity, Map<String, String>> benefitItemIdx;

    public PercentDiscountBenefitItemsRepository() {
        super();
        benefitItemIdx = new HashMap<>();
    }

    @Override
    public PercentDiscountBenefitItemsEntity save(PercentDiscountBenefitItemsEntity entity) {
        if (benefitItemIdx.containsKey(entity.getBenefit()) &&
                benefitItemIdx.get(entity.getBenefit()).containsKey(entity.getItemID())) {
            throw new DuplicateEntriesException(PercentDiscountBenefitItemsEntity.class, null);
        }
        entity = super.save(entity);
        benefitItemIdx.putIfAbsent(entity.getBenefit(), new HashMap<>());
        benefitItemIdx.get(entity.getBenefit()).put(entity.getItemID(), entity.getId());
        return entity;
    }

    @Override
    public PercentDiscountBenefitItemsEntity delete(PercentDiscountBenefitItemsEntity entity) {
        if (!benefitItemIdx.containsKey(entity.getBenefit()) ||
                !benefitItemIdx.get(entity.getBenefit()).containsKey(entity.getItemID())) {
            throw new EntityNotFoundException(PercentDiscountBenefitItemsEntity.class, null);
        }
        entity = super.delete(entity);
        benefitItemIdx.get(entity.getBenefit()).remove(entity.getItemID());
        return entity;
    }

    public List<PercentDiscountBenefitItemsEntity> getAllByBenefit(PlanBenefitEntity planBenefit) {
        return benefitItemIdx.getOrDefault(planBenefit, new HashMap<>())
                .values()
                .stream()
                .map(id -> getById(id).orElseThrow())
                .toList();
    }
}
