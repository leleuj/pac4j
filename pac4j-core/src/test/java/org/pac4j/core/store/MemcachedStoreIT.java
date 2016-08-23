package org.pac4j.core.store;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedClientIF;
import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsHelper;

import java.io.IOException;

/**
 * Test {@link MemcachedStore}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class MemcachedStoreIT extends AbstractStoreTests<MemcachedStore> {

    private MemcachedClientIF getClient() {
        try {
            return new MemcachedClient(new BinaryConnectionFactory(),
                    AddrUtil.getAddresses("memcached-14400.c8.us-east-1-3.ec2.cloud.redislabs.com:14400"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected MemcachedStore buildStore() {
        return new MemcachedStore(getClient(), 1);
    }

    @Test
    public void testNoClient() {
        final MemcachedStore store = new MemcachedStore();
        store.setTimeoutSeconds(10);
        TestsHelper.expectException(store::init, TechnicalException.class, "client cannot be null");
    }

    @Test
    public void testNoTimeout() {
        final MemcachedStore store = new MemcachedStore();
        store.setClient(getClient());
        TestsHelper.expectException(store::init, TechnicalException.class, "timeoutSeconds must be greater than zero");
    }

    @Test
    public void testNoSerializer() {
        final MemcachedStore store = new MemcachedStore();
        store.setTimeoutSeconds(5);
        store.setClient(getClient());
        store.setSerializer(null);
        TestsHelper.expectException(store::init, TechnicalException.class, "serializer cannot be null");
    }
}
