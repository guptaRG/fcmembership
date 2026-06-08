package model.request;

import entity.MembershipPlanEntity;
import util.StringUtil;

import java.time.Duration;
import java.util.List;

public record CreatePlanRequest(int pricePaise, String name, Duration duration, boolean recurring,
                                String description, List<CreatePlanTierRequest> tiers) {

    public boolean isValid() {
        return pricePaise >= 0 && !duration.isNegative() && !duration.isZero();
    }

    public MembershipPlanEntity toPlanEntity() {
        return new MembershipPlanEntity(pricePaise, name, duration, recurring, description);
    }
}
