package model.enums;

import java.util.*;

public enum SubscriptionStatus {
    NEW_LEAD,
    ACTIVE,
    CANCELED,
    PAUSED;

    private static final Map<SubscriptionStatus, Set<SubscriptionStatus>> statusTransitionMap =
            new EnumMap<>(SubscriptionStatus.class);
    static {
        statusTransitionMap.put(NEW_LEAD, Set.of(ACTIVE));
        statusTransitionMap.put(ACTIVE, Set.of(PAUSED, CANCELED));
        statusTransitionMap.put(PAUSED, Set.of(ACTIVE, CANCELED));
    }

    public boolean isValidStatusTransition(SubscriptionStatus fromStatus,  SubscriptionStatus toStatus) {
        return statusTransitionMap.getOrDefault(fromStatus, new HashSet<>()).contains(toStatus);
    }
}
