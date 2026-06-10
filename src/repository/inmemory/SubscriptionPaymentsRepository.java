package repository.inmemory;

import entity.SubscriptionEntity;
import entity.SubscriptionPaymentsEntity;
import exception.EntityNotFoundException;

import java.util.*;

public class SubscriptionPaymentsRepository extends AbstractInMemoryRepository<SubscriptionPaymentsEntity> {
    private final Map<SubscriptionEntity, Set<String>> paymentsBySubscription;

    public SubscriptionPaymentsRepository() {
        super();
        paymentsBySubscription = new HashMap<>();
    }

    @Override
    public SubscriptionPaymentsEntity save(SubscriptionPaymentsEntity subscriptionPaymentsEntity) {
        subscriptionPaymentsEntity = super.save(subscriptionPaymentsEntity);
        paymentsBySubscription.putIfAbsent(subscriptionPaymentsEntity.getSubscription(), new HashSet<>());
        paymentsBySubscription.get(subscriptionPaymentsEntity.getSubscription())
                .add(subscriptionPaymentsEntity.getId());
        return subscriptionPaymentsEntity;
    }

    @Override
    public SubscriptionPaymentsEntity delete(SubscriptionPaymentsEntity subscriptionPaymentsEntity) {
        if (!paymentsBySubscription.containsKey(subscriptionPaymentsEntity.getSubscription()) ||
                !paymentsBySubscription
                        .get(subscriptionPaymentsEntity.getSubscription())
                        .contains(subscriptionPaymentsEntity.getId())) {

            throw new EntityNotFoundException(SubscriptionPaymentsEntity.class, null);
        }
        subscriptionPaymentsEntity = super.delete(subscriptionPaymentsEntity);
        paymentsBySubscription.get(subscriptionPaymentsEntity.getSubscription())
                .remove(subscriptionPaymentsEntity.getId());
        return subscriptionPaymentsEntity;
    }

    public List<SubscriptionPaymentsEntity> getAllBySubscription(SubscriptionEntity subscriptionEntity) {
        return paymentsBySubscription.getOrDefault(subscriptionEntity, new HashSet<>())
                .stream()
                .map(id -> getById(id).orElseThrow())
                .toList();
    }
}
