package org.pac4j.core.store;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import java.util.concurrent.TimeUnit;

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
                .withCache(NAME, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(100, TimeUnit.MILLISECONDS))))
                .build();
        cacheManager.init();
        return new EhCacheStore(cacheManager.getCache(NAME, String.class, String.class));
    }

    @Test
    public void testSetRemoveGet() {
        final EhCacheStore store = buildStore();
        store.set(KEY, VALUE);
        assertEquals(VALUE, store.get(KEY));
        store.remove(KEY);
        assertNull(store.get(KEY));
    }

    @Test
    public void testSetExpiredGet() throws Exception {
        final EhCacheStore store = buildStore();
        store.set(KEY, VALUE);
        assertEquals(VALUE, store.get(KEY));
        Thread.sleep(200);
        assertNull(store.get(KEY));
    }

    @Test
    public void testSetNullValue() {
        final EhCacheStore store = buildStore();
        TestsHelper.expectException(() -> store.set(KEY, null), TechnicalException.class, "value cannot be null");
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
