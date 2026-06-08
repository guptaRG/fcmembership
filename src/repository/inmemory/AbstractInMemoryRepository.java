package repository.inmemory;

import entity.BaseEntity;
import exception.EntityNotFoundException;
import exception.InvalidRequestException;
import repository.BaseRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractInMemoryRepository<T extends BaseEntity> implements BaseRepository<T> {
    protected final Map<String, T> store;

    /**
     * Default constructor to initialise the store.
     */
    public AbstractInMemoryRepository() {
        this.store = new ConcurrentHashMap<>();
    }

    /**
     * Saves a new object in the store.
     * If the passed object already exists in the store an
     * {@link exception.InvalidRequestException InvalidRequestException} is thrown.
     * @param obj object to be saved.
     * @return the same object which has been saved.
     */
    @Override
    public T save(T obj) {
        if (getById(obj.getId()).isPresent()) {
            throw new InvalidRequestException(String.format("An object with %s ID is already present in the store",
                    obj.getId()), null);
        }
        store.put(obj.getId(), obj);
        return obj;
    }

    @Override
    public List<T> saveAll(List<T> obj) {
        return obj.stream().parallel().map(this::save).collect(Collectors.toList());
    }

    /**
     * Updates the given object in the store.
     * If the passed object doesn't exist in the store an
     * {@link exception.EntityNotFoundException EntityNotFoundException} is thrown.
     * @param obj object to be updated.
     * @return the same object which has been updated.
     */
    @Override
    public T update(T obj) {
        if (getById(obj.getId()).isEmpty()) {
            throw new EntityNotFoundException(obj.getClass(), null);
        }
        obj.setUpdatedAt(new Date());
        store.put(obj.getId(), obj);
        return obj;
    }

    /**
     * Deletes the given object in the store by setting active to false.
     * If the passed object doesn't exist in the store an
     * {@link exception.EntityNotFoundException EntityNotFoundException} is thrown.
     * @param obj object to be updated.
     * @return the same object which has been deleted.
     */
    @Override
    public T delete(T obj) {
        if (!store.containsKey(obj.getId())) {
            throw new EntityNotFoundException(obj.getClass(), null);
        }
        obj.setUpdatedAt(new Date());
        obj.setActive(false);
        store.put(obj.getId(), obj);
        return obj;
    }

    /**
     * Gets active object from the store by the given ID.
     * @param id ID of the object.
     * @return stored object if found active for the passed id, empty optional otherwise.
     */
    @Override
    public Optional<T> getById(String id) {
        T obj = store.get(id);
        if (Objects.isNull(obj) || !obj.isActive()) {
            return Optional.empty();
        }
        return Optional.of(obj);
    }

    /**
     * Gets all active objects from the store.
     * @return List of all active objects from the store.
     */
    @Override
    public List<T> getAll() {
        return store.values().stream().filter(T::isActive).collect(Collectors.toList());
    }

    /**
     * Gets all active objects from the store sorted by their {@link entity.BaseEntity.createdAt createdAt}.
     * @return List of all active objects from the store sorted by their {@link entity.BaseEntity.createdAt createdAt}.
     */
    @Override
    public List<T> getAllOrderByCreatedAt() {
        List<T> allValues = getAll();
        allValues.sort(Comparator.comparing(T::getCreatedAt));
        return allValues;
    }

    /**
     * Gets all active objects from the store sorted by their {@link entity.BaseEntity.updatedAt updatedAt}.
     * @return List of all active objects from the store sorted by their {@link entity.BaseEntity.updatedAt updatedAt}.
     */
    @Override
    public List<T> getAllOrderByUpdatedAt() {
        List<T> allValues = getAll();
        allValues.sort(Comparator.comparing(T::getUpdatedAt));
        return allValues;
    }
}
