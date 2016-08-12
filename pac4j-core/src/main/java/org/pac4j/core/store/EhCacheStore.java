package org.pac4j.core.store;

import org.ehcache.Cache;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.InitializableObject;

/**
 * Store data in EhCache.
 *
 * Add the <code>ehcache</code> dependency to use this store.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class EhCacheStore<K, O> extends InitializableObject implements Store<K, O> {

    private Cache<K, O> cache;

    public EhCacheStore() {}

    public EhCacheStore(final Cache<K, O> cache) {
        this.cache = cache;
    }

    @Override
    protected void internalInit() {
        CommonHelper.assertNotNull("cache", cache);
    }

    @Override
    public O get(final K key) {
        init();
        if (key != null) {
            return cache.get(key);
        }
        return null;
    }

    @Override
    public void set(final K key, final O value) {
        init();
        CommonHelper.assertNotNull("value", value);

        if (key != null) {
            cache.put(key, value);
        }
    }

    @Override
    public void remove(final K key) {
        init();
        if (key != null) {
            cache.remove(key);
        }
    }

    public Cache<K, O> getCache() {
        return this.cache;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "cache", cache);
    }
}
