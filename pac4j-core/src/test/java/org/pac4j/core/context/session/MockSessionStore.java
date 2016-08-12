package org.pac4j.core.context.session;

import org.pac4j.core.context.WebContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock a session store in memory.
 *
 * @author Jerome Leleu
 * @since 1.9.0
 */
public class MockSessionStore implements SessionStore {

    protected Map<String, Object> store = new HashMap<>();

    protected String id;

    public MockSessionStore() {}

    public MockSessionStore(final Map<String, Object> store) {
        this.store = store;
    }

    @Override
    public String getOrCreateSessionId(final WebContext context) {
        if (id == null) {
            id = new Date().toString();
        }
        return id;
    }

    @Override
    public Object get(final WebContext context, final String key) {
        return store.get(key);
    }

    @Override
    public void set(final WebContext context, final String key, final Object value) {
        store.put(key, value);
    }

    @Override
    public void invalidateSession(final WebContext context) {
        store.clear();
    }

    @Override
    public  Object getTrackableObject(final WebContext context) {
        return store;
    }

    @Override
    public SessionStore<WebContext> renewFromTrackableObject(final WebContext context, final Object trackableSession) {
        return new MockSessionStore((Map<String, Object>) trackableSession);
    }
}
