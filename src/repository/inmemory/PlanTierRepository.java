package repository.inmemory;

import entity.MembershipPlanEntity;
import entity.PlanTierEntity;
import exception.EntityNotFoundException;

import java.util.*;

public class PlanTierRepository extends AbstractInMemoryRepository<PlanTierEntity> {
    private final Map<MembershipPlanEntity, Set<String>> planTierByPlan;

    public PlanTierRepository() {
        super();
        planTierByPlan = new HashMap<>();
    }

    @Override
    public PlanTierEntity save(PlanTierEntity entity) {
        entity = super.save(entity);
        planTierByPlan.putIfAbsent(entity.getPlan(), new HashSet<>());
        planTierByPlan.get(entity.getPlan()).add(entity.getId());
        return entity;
    }

    @Override
    public PlanTierEntity delete(PlanTierEntity entity) {
        if (!planTierByPlan.containsKey(entity.getPlan()) ||
                !planTierByPlan.get(entity.getPlan()).contains(entity.getId())) {
            throw new EntityNotFoundException(PlanTierEntity.class, null);
        }
        entity = super.delete(entity);
        planTierByPlan.get(entity.getPlan()).remove(entity.getId());
        return entity;
    }

    public List<PlanTierEntity> getAllByPlan(MembershipPlanEntity membershipPlan) {
        return planTierByPlan.getOrDefault(membershipPlan, new HashSet<>())
                .stream()
                .map(id -> getById(id).orElseThrow())
                .toList();
    }
}
