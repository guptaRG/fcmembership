package entity;

import java.time.Duration;

public class MembershipPlanEntity extends BaseEntity {
    private final int pricePaise;
    private final String name;
    private final Duration duration;
    private final boolean recurring;

    public MembershipPlanEntity(int pricePaise, String name, Duration duration, boolean recurring) {
        super();
        this.pricePaise = pricePaise;
        this.name = name;
        this.duration = duration;
        this.recurring = recurring;
    }

    public int getPricePaise() {
        return pricePaise;
    }

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean isRecurring() {
        return recurring;
    }
}
