package model.bo;

import java.time.Duration;
import java.util.List;

public record MembershipPlanBO(int pricePaise, String name, Duration duration, boolean recurring,
                               String description, List<PlanTierBO> tiers, String planID) {

    @Override
    public String toString() {
        return "MembershipPlanBO{" +
                "pricePaise=" + pricePaise +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", recurring=" + recurring +
                ", description='" + description + '\'' +
                ", tiers=" + tiers +
                ", planID='" + planID + '\'' +
                '}';
    }
}
