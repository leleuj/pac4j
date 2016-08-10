package org.pac4j.core.store;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test {@link EhCacheStore}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class EhCacheStoreTests implements TestsConstants {

    private EhCacheStore buildStore() {
        final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(NAME, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10)))
                .build();
        cacheManager.init();
        return new EhCacheStore(cacheManager.getCache(NAME, String.class, String.class));
    }

    @Test
    public void testSetGet() {
        final EhCacheStore store = buildStore();
        store.set(KEY, VALUE);
        assertEquals(VALUE, store.get(KEY));
    }

    @Test
    public void testMissingObject() {
        final EhCacheStore store = buildStore();
        assertNull(store.get(KEY));
    }

    @Test
    public void testNoCache() {
        final EhCacheStore store = new EhCacheStore();
        TestsHelper.expectException(store::init, TechnicalException.class, "cache cannot be null");
    }
}
