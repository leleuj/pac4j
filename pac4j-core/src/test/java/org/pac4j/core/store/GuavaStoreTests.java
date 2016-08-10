package org.pac4j.core.store;

import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test {@link GuavaStore}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class GuavaStoreTests implements TestsConstants {

    private GuavaStore buildStore() {
        return new GuavaStore(10, 20, TimeUnit.SECONDS);
    }

    @Test
    public void testSetGet() {
        final GuavaStore store = buildStore();
        store.set(KEY, VALUE);
        assertEquals(VALUE, store.get(KEY));
    }

    @Test
    public void testMissingObject() {
        final GuavaStore store = buildStore();
        assertNull(store.get(KEY));
    }

    @Test
    public void testBadSize() {
        final GuavaStore store = new GuavaStore();
        store.setTimeout(15);
        store.setTimeUnit(TimeUnit.SECONDS);
        TestsHelper.expectException(store::init, TechnicalException.class, "size mut be greater than zero");
    }

    @Test
    public void testBadTimeout() {
        final GuavaStore store = new GuavaStore();
        store.setSize(15);
        store.setTimeUnit(TimeUnit.SECONDS);
        TestsHelper.expectException(store::init, TechnicalException.class, "timeout must be greater than zero");
    }

    @Test
    public void testBadTimeUnit() {
        final GuavaStore store = new GuavaStore();
        store.setSize(15);
        store.setTimeout(20);
        TestsHelper.expectException(store::init, TechnicalException.class, "timeUnit cannot be null");
    }
}
