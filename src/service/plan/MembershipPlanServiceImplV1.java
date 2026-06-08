package service.plan;

import adaptor.MembershipPlanAdaptor;
import adaptor.PlanTierAdaptor;
import entity.MembershipPlanEntity;
import entity.PlanBenefitEntity;
import entity.PlanTierEntity;
import model.bo.MembershipPlanBO;
import model.bo.PlanTierBO;
import model.bo.PlanTierBenefitBO;
import model.request.CreatePlanRequest;
import repository.inmemory.MembershipPlanRepository;
import repository.inmemory.PlanBenefitRepository;
import repository.inmemory.PlanTierRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MembershipPlanServiceImplV1 implements MembershipPlanService {
    private final MembershipPlanRepository membershipPlanRepository;
    private final PlanTierRepository planTierRepository;
    private final PlanBenefitRepository planBenefitRepository;

    public MembershipPlanServiceImplV1(MembershipPlanRepository membershipPlanRepository,
                                       PlanTierRepository planTierRepository,
                                       PlanBenefitRepository planBenefitRepository) {
        this.membershipPlanRepository = membershipPlanRepository;
        this.planTierRepository = planTierRepository;
        this.planBenefitRepository = planBenefitRepository;
    }

    @Override
    public List<MembershipPlanBO> getAllPlans(String userID) {
        return membershipPlanRepository.getAll().stream().map(membershipPlan -> {
            List<PlanTierBO> planTiers = planTierRepository.getAllByPlan(membershipPlan)
                    .stream()
                    .map(planTier ->
                        new PlanTierBO(planTier.getTier(), planTier.getTierName(), planTier.getTierDescription(),
                                planBenefitRepository.getAllByPlanTier(planTier), planTier.getAdditionalPaymentPaise(),
                                planTier.getId()))
                    .toList();
            return new MembershipPlanBO(membershipPlan.getPricePaise(), membershipPlan.getName(),
                    membershipPlan.getDuration(), membershipPlan.isRecurring(), membershipPlan.getDescription(),
                    planTiers, membershipPlan.getId());
        }).toList();
    }

    @Override
    public MembershipPlanBO createPlan(CreatePlanRequest planRequest) {
        MembershipPlanEntity membershipPlanEntity = membershipPlanRepository.save(planRequest.toPlanEntity());
        List<PlanTierEntity> tiers = new ArrayList<>();
        List<PlanTierBO> tierBOs = new ArrayList<>();
        List<PlanBenefitEntity> benefits = new ArrayList<>();
        planRequest.tiers()
                .forEach(tier -> {
                    PlanTierEntity tierEntity = PlanTierAdaptor.createPlanTierRequestToEntity(tier,
                            membershipPlanEntity);
                    tiers.add(tierEntity);
                    List<PlanBenefitEntity> benefitEntities = tier.benefits().stream().map(benefit ->
                            new PlanBenefitEntity(benefit.benefit(), tierEntity, benefit.metadata())).toList();
                    benefits.addAll(benefitEntities);
                    tierBOs.add(PlanTierAdaptor.planTierEntityToBO(tierEntity, benefitEntities));
                });
        try {
            planTierRepository.saveAll(tiers);
        } catch (Exception e) {
            membershipPlanRepository.delete(membershipPlanEntity);
            throw e;
        }
        try {
            planBenefitRepository.saveAll(benefits);
            return MembershipPlanAdaptor.planEntityToBO(membershipPlanEntity, tierBOs);
        } catch (Exception e) {
            tiers.forEach(planTierRepository::delete);
            membershipPlanRepository.delete(membershipPlanEntity);
            throw e;
        }
    }
}
