package repository.inmemory;

import entity.MembershipPlanEntity;
import entity.PlanTierEntity;
import exception.DuplicateEntriesException;
import exception.EntityNotFoundException;

import java.util.*;

public class PlanTierRepository extends AbstractInMemoryRepository<PlanTierEntity> {
    private final Map<MembershipPlanEntity, Map<Integer, String>> planTierByPlanTierNum;

    public PlanTierRepository() {
        super();
        planTierByPlanTierNum = new HashMap<>();
    }

    @Override
    public PlanTierEntity save(PlanTierEntity entity) {
        planTierByPlanTierNum.putIfAbsent(entity.getPlan(), new HashMap<>());
        if (planTierByPlanTierNum.get(entity.getPlan()).containsKey(entity.getTier())) {
            throw new DuplicateEntriesException(PlanTierEntity.class, null);
        }
        entity = super.save(entity);
        planTierByPlanTierNum.get(entity.getPlan()).put(entity.getTier(), entity.getId());
        return entity;
    }

    @Override
    public PlanTierEntity delete(PlanTierEntity entity) {
        if (!planTierByPlanTierNum.containsKey(entity.getPlan()) ||
                !planTierByPlanTierNum.get(entity.getPlan()).containsKey(entity.getTier())) {
            throw new EntityNotFoundException(PlanTierEntity.class, null);
        }
        entity = super.delete(entity);
        planTierByPlanTierNum.get(entity.getPlan()).remove(entity.getTier());
        return entity;
    }

    public List<PlanTierEntity> getAllByPlan(MembershipPlanEntity membershipPlan) {
        return planTierByPlanTierNum.getOrDefault(membershipPlan, new HashMap<>())
                .values()
                .stream()
                .map(id -> getById(id).orElseThrow())
                .toList();
    }

    public Optional<PlanTierEntity> getByPlanTier(MembershipPlanEntity plan, int tier) {
        if (!planTierByPlanTierNum.containsKey(plan) || !planTierByPlanTierNum.get(plan).containsKey(tier)) {
            return Optional.empty();
        }
        return getById(planTierByPlanTierNum.get(plan).get(tier));
    }
}
