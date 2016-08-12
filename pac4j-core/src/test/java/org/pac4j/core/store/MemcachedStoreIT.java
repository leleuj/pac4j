package org.pac4j.core.store;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedClientIF;
import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test {@link MemcachedStore}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class MemcachedStoreIT implements TestsConstants {

    private MemcachedClientIF getClient() {
        try {
            return new MemcachedClient(new BinaryConnectionFactory(),
                    AddrUtil.getAddresses("memcached-14400.c8.us-east-1-3.ec2.cloud.redislabs.com:14400"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MemcachedStore buildStore() {
        return new MemcachedStore(getClient(), 1);
    }

    @Test
    public void testSetRemoveGet() {
        final MemcachedStore store = buildStore();
        store.set(KEY, VALUE);
        assertEquals(VALUE, store.get(KEY));
        store.remove(KEY);
        assertNull(store.get(KEY));
    }

    @Test
    public void testSetExpiredGet() throws Exception {
        final MemcachedStore store = buildStore();
        store.set(KEY, VALUE);
        assertEquals(VALUE, store.get(KEY));
        Thread.sleep(2000);
        assertNull(store.get(KEY));
    }

    @Test
    public void testSetNullValue() {
        final MemcachedStore store = buildStore();
        TestsHelper.expectException(() -> store.set(KEY, null), TechnicalException.class, "value cannot be null");
    }

    @Test
    public void testMissingObject() {
        final MemcachedStore store = buildStore();
        assertNull(store.get(KEY));
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
