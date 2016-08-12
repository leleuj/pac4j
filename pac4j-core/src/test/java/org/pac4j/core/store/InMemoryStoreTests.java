package org.pac4j.core.store;

import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import static org.junit.Assert.*;

/**
 * Test {@link InMemoryStore}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class InMemoryStoreTests implements TestsConstants {

    private InMemoryStore buildStore() {
        return new InMemoryStore();
    }

    @Test
    public void testSetRemoveGet() {
        final InMemoryStore store = buildStore();
        store.set(KEY, VALUE);
        assertEquals(VALUE, store.get(KEY));
        store.remove(KEY);
        assertNull(store.get(KEY));
    }

    @Test
    public void testSetNullValue() {
        final InMemoryStore store = buildStore();
        TestsHelper.expectException(() -> store.set(KEY, null), TechnicalException.class, "value cannot be null");
    }

    @Test
    public void testMissingObject() {
        final InMemoryStore store = buildStore();
        assertNull(store.get(KEY));
    }
}
