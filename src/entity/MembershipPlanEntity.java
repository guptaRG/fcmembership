package entity;

import java.time.Duration;

public class MembershipPlanEntity extends BaseEntity {
    private final int pricePaise;
    private final String name;
    private final Duration duration;
    private final boolean recurring;
    private String description;

    public MembershipPlanEntity(int pricePaise, String name, Duration duration, boolean recurring, String description) {
        super();
        this.pricePaise = pricePaise;
        this.name = name;
        this.duration = duration;
        this.recurring = recurring;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MembershipPlanEntity{" +
                "pricePaise=" + pricePaise +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
