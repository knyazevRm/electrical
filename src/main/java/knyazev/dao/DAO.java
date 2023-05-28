package knyazev.dao;

import java.util.Optional;

public interface DAO<K, V extends IdentityInterface<K>> {
    void put(V object);
    Optional<V> get(K key);
}
