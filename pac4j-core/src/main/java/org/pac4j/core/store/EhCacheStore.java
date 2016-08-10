package org.pac4j.core.store;

import org.ehcache.Cache;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.InitializableObject;

import java.io.Serializable;

/**
 * Store data in EhCache.
 *
 * Add the <code>ehcache</code> dependency to use this store.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class EhCacheStore<K extends Serializable, O extends Serializable> extends InitializableObject implements Store<K, O> {

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

        return cache.get(key);
    }

    @Override
    public void set(final K key, final O value) {
        init();

        cache.put(key, value);
    }

    public Cache<K, O> getCache() {
        return this.cache;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "cache", cache);
    }
}
