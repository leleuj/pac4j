package org.pac4j.core.store;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Store data in memory. Only for tests purposes.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class InMemoryStore<K, O> extends AbstractMemoryStore<K, O> {

    private final ConcurrentHashMap<K, O> map = new ConcurrentHashMap<>();

    public InMemoryStore() {}

    @Override
    protected O internalGet(final K key) {
        return map.get(key);
    }

    @Override
    protected void internalSet(final K key, final O value) {
        map.put(key, value);
    }

    @Override
    protected void internalRemove(final K key) {
        map.remove(key);
    }

    public ConcurrentHashMap<K, O> getCache() {
        return this.map;
    }
}
