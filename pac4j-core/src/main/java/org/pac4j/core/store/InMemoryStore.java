package org.pac4j.core.store;

import org.pac4j.core.util.CommonHelper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Store data in memory. Only for tests purposes.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class InMemoryStore<K, O> implements Store<K, O> {

    private final ConcurrentHashMap<K, O> map = new ConcurrentHashMap<>();

    public InMemoryStore() {}

    @Override
    public O get(final K key) {
        if (key != null) {
            return map.get(key);
        }
        return null;
    }

    @Override
    public void set(final K key, final O value) {
        CommonHelper.assertNotNull("value", value);

        if (key != null) {
            map.put(key, value);
        }
    }

    @Override
    public void remove(final K key) {
        if (key != null) {
            map.remove(key);
        }
    }

    public ConcurrentHashMap<K, O> getCache() {
        return this.map;
    }
}
