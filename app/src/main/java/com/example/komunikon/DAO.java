package com.example.komunikon;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> get(long id);

    List<T> getAll();

    void save(T t);

    void update(int id, T t);

    void delete(T t);
}
