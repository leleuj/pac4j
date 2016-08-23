package org.pac4j.core.store;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsHelper;

import java.util.concurrent.TimeUnit;

/**
 * Test {@link EhCacheStore}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class EhCacheStoreTests extends AbstractStoreTests<EhCacheStore> {

    protected EhCacheStore buildStore() {
        final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(NAME, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.SECONDS))))
                .build();
        cacheManager.init();
        return new EhCacheStore(cacheManager.getCache(NAME, String.class, String.class));
    }

    @Test
    public void testNoCache() {
        final EhCacheStore store = new EhCacheStore();
        TestsHelper.expectException(store::init, TechnicalException.class, "cache cannot be null");
    }
}
