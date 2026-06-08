package adaptor;

import entity.MembershipPlanEntity;
import model.bo.MembershipPlanBO;
import model.bo.PlanTierBO;

import java.util.List;

public class MembershipPlanAdaptor {

    public static MembershipPlanBO planEntityToBO(MembershipPlanEntity planEntity, List<PlanTierBO> tiers) {
        return new MembershipPlanBO(planEntity.getPricePaise(), planEntity.getName(), planEntity.getDuration(),
                planEntity.isRecurring(), planEntity.getDescription(), tiers, planEntity.getId());
    }
}
