package repository.inmemory;

import entity.PlanBenefitEntity;
import entity.PlanTierEntity;
import exception.EntityNotFoundException;

import java.util.*;

public class PlanBenefitRepository extends AbstractInMemoryRepository<PlanBenefitEntity> {
    private final Map<PlanTierEntity, Set<String>> benefitsByPlanTier;

    public PlanBenefitRepository() {
        super();
        this.benefitsByPlanTier = new HashMap<>();
    }

    @Override
    public PlanBenefitEntity save(PlanBenefitEntity benefit) {
        benefit = super.save(benefit);
        benefitsByPlanTier.putIfAbsent(benefit.getPlanTier(), new HashSet<>());
        benefitsByPlanTier.get(benefit.getPlanTier()).add(benefit.getId());
        return benefit;
    }

    @Override
    public PlanBenefitEntity delete(PlanBenefitEntity benefit) {
        if (!benefitsByPlanTier.containsKey(benefit.getPlanTier()) ||
                !benefitsByPlanTier.get(benefit.getPlanTier()).contains(benefit.getId())) {
            throw new EntityNotFoundException(PlanBenefitEntity.class, null);
        }
        benefit = super.delete(benefit);
        benefitsByPlanTier.get(benefit.getPlanTier()).remove(benefit.getId());
        return benefit;
    }

    public List<PlanBenefitEntity> getAllByPlanTier(PlanTierEntity planTier) {
        return benefitsByPlanTier.getOrDefault(planTier, new HashSet<>())
                .stream()
                .map(id -> getById(id).orElseThrow())
                .toList();
    }
}
