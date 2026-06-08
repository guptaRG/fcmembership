package service.plan;

import model.bo.MembershipPlanBO;
import model.request.CreatePlanRequest;

import java.util.List;

public interface MembershipPlanService {
    List<MembershipPlanBO> getAllPlans(String userID);
    MembershipPlanBO createPlan(CreatePlanRequest planRequest);
}
