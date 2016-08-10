package org.pac4j.core.store;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Store data in memory. Only for tests purposes.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class InMemoryStore<K extends Serializable, O extends Serializable> implements Store<K, O> {

    private final ConcurrentHashMap<K, O> map = new ConcurrentHashMap<>();

    public InMemoryStore() {}

    @Override
    public O get(final K key) {
        return map.get(key);
    }

    @Override
    public void set(final K key, final O value) {
        map.put(key, value);
    }

    public ConcurrentHashMap<K, O> getCache() {
        return this.map;
    }
}
