package org.pac4j.core.store;

import org.ehcache.Cache;
import org.pac4j.core.util.CommonHelper;

/**
 * Store data in EhCache.
 *
 * Add the <code>ehcache</code> dependency to use this store.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class EhCacheStore<K, O> extends AbstractMemoryStore<K, O> {

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
    protected O internalGet(final K key) {
        return cache.get(key);
    }

    @Override
    protected void internalSet(final K key, final O value) {
        cache.put(key, value);
    }

    @Override
    protected void internalRemove(final K key) {
        cache.remove(key);
    }

    public Cache<K, O> getCache() {
        return this.cache;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "cache", cache);
    }
}
