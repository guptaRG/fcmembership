package repository.inmemory;

import entity.SubscriptionEntity;
import exception.DuplicateEntriesException;
import exception.EntityNotFoundException;
import model.enums.SubscriptionStatus;

import java.util.*;

public class SubscriptionRepository extends AbstractInMemoryRepository<SubscriptionEntity> {
    private final Map<String, Map<SubscriptionStatus, Set<String>>> subscriptions;

    public SubscriptionRepository() {
        super();
        subscriptions = new HashMap<>();
    }

    @Override
    public SubscriptionEntity save(SubscriptionEntity subscriptionEntity) {
        subscriptions.putIfAbsent(subscriptionEntity.getUserID(), new EnumMap<>(SubscriptionStatus.class));
        if (subscriptions.get(subscriptionEntity.getUserID())
                .getOrDefault(subscriptionEntity.getStatus(), new HashSet<>())
                .contains(subscriptionEntity.getId())) {
            throw new DuplicateEntriesException(SubscriptionEntity.class, null);
        }
        subscriptionEntity = super.save(subscriptionEntity);
        subscriptions.get(subscriptionEntity.getUserID()).putIfAbsent(subscriptionEntity.getStatus(), new HashSet<>());
        subscriptions.get(subscriptionEntity.getUserID())
                .get(subscriptionEntity.getStatus())
                .add(subscriptionEntity.getId());
        return subscriptionEntity;
    }

    @Override
    public SubscriptionEntity update(SubscriptionEntity subscriptionEntity) {
        if (!subscriptions.containsKey(subscriptionEntity.getUserID())) {
            throw new EntityNotFoundException(SubscriptionEntity.class, null);
        }
        SubscriptionEntity oldSubscription = getById(subscriptionEntity.getId()).orElseThrow();
        if (!subscriptions.get(subscriptionEntity.getUserID()).containsKey(oldSubscription.getStatus()) ||
                !subscriptions.get(subscriptionEntity.getUserID())
                        .get(oldSubscription.getStatus())
                        .contains(oldSubscription.getId())) {
            throw new EntityNotFoundException(SubscriptionEntity.class, null);
        }
        subscriptionEntity = super.update(subscriptionEntity);
        if (oldSubscription.getStatus() != subscriptionEntity.getStatus()) {
            subscriptions.get(oldSubscription.getUserID())
                    .get(oldSubscription.getStatus())
                    .remove(oldSubscription.getId());
            subscriptions.get(subscriptionEntity.getUserID()).putIfAbsent(subscriptionEntity.getStatus(),
                    new HashSet<>());
            subscriptions.get(subscriptionEntity.getUserID())
                    .get(subscriptionEntity.getStatus())
                    .add(subscriptionEntity.getId());
        }
        return subscriptionEntity;
    }

    @Override
    public SubscriptionEntity delete(SubscriptionEntity subscriptionEntity) {
        if (!subscriptions.containsKey(subscriptionEntity.getUserID()) ||
                !subscriptions.get(subscriptionEntity.getUserID()).containsKey(subscriptionEntity.getStatus()) ||
                !subscriptions
                        .get(subscriptionEntity.getUserID())
                        .get(subscriptionEntity.getStatus())
                        .contains(subscriptionEntity.getId())) {
            throw new EntityNotFoundException(SubscriptionEntity.class, null);
        }
        subscriptionEntity = super.delete(subscriptionEntity);
        subscriptions.get(subscriptionEntity.getUserID())
                .get(subscriptionEntity.getStatus())
                .remove(subscriptionEntity.getId());
        return subscriptionEntity;
    }

    public List<SubscriptionEntity> getAllByUserSubscriptionStatus(String userID, SubscriptionStatus status) {
        return subscriptions.getOrDefault(userID, new EnumMap<>(SubscriptionStatus.class))
                .getOrDefault(status, new HashSet<>())
                .stream()
                .map(id -> getById(id).orElseThrow())
                .toList();
    }
}
