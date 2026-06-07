package entity;

import java.util.Date;
import java.util.UUID;

public class BaseEntity {
    private final String id;
    private final Date createdAt;
    private Date updatedAt;
    private boolean active;

    public BaseEntity() {
        id = UUID.randomUUID().toString();
        this.active = true;
        createdAt = new Date();
        updatedAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }
}
