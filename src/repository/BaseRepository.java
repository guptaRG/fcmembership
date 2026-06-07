package repository;

import entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {
    T save(T obj);
    List<T> saveAll(List<T> objs);
    T update(T obj);
    T delete(T obj);
    Optional<T> getById(String id);
    List<T> getAll();
    List<T> getAllOrderByCreatedAt();
    List<T> getAllOrderByUpdatedAt();
}
