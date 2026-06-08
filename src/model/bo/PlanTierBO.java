package model.bo;

import entity.PlanBenefitEntity;

import java.util.List;

public record PlanTierBO(int tier, String tierName, String description, List<PlanBenefitEntity> benefits,
                         int additionalPaymentPaise, String id) {

    @Override
    public String toString() {
        return "PlanTierBO{" +
                "tier=" + tier +
                ", tierName='" + tierName + '\'' +
                ", description='" + description + '\'' +
                ", benefits=" + benefits +
                ", additionalPaymentPaise=" + additionalPaymentPaise +
                ", id='" + id + '\'' +
                '}';
    }
}
