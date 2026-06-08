package adaptor;

import entity.MembershipPlanEntity;
import entity.PlanBenefitEntity;
import entity.PlanTierEntity;
import model.bo.PlanTierBO;
import model.request.CreatePlanTierRequest;

import java.util.List;

public class PlanTierAdaptor {

    public static PlanTierEntity createPlanTierRequestToEntity(CreatePlanTierRequest createPlanTierRequest,
                                                               MembershipPlanEntity planEntity) {
        return new PlanTierEntity(planEntity, createPlanTierRequest.tier(), createPlanTierRequest.tierName(),
                createPlanTierRequest.description(), createPlanTierRequest.additionalPaymentPaise());
    }

    public static PlanTierBO planTierEntityToBO(PlanTierEntity planTierEntity,
                                                List<PlanBenefitEntity> benefitEntities) {
        return new PlanTierBO(planTierEntity.getTier(), planTierEntity.getTierName(),
                planTierEntity.getTierDescription(), benefitEntities, planTierEntity.getAdditionalPaymentPaise(),
                planTierEntity.getId());
    }
}
