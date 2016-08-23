package org.pac4j.core.store;

import org.junit.Test;

/**
 * Test {@link InMemoryStore}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class InMemoryStoreTests extends AbstractStoreTests<InMemoryStore> {

    protected InMemoryStore buildStore() {
        return new InMemoryStore();
    }

    @Test
    public void testSetExpiredGet() throws Exception {
        // no expiration concept in the InMemoryStore
    }
}
