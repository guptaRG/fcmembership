package model.request;

import entity.MembershipPlanEntity;

import java.time.Duration;
import java.util.List;

public record CreatePlanRequest(int pricePaise, String name, Duration duration, boolean recurring,
                                String description, List<CreatePlanTierRequest> tiers) {

    public MembershipPlanEntity toPlanEntity() {
        return new MembershipPlanEntity(pricePaise, name, duration, recurring, description);
    }
}
