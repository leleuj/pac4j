package org.pac4j.cas.logout;

import org.jasig.cas.client.session.SingleSignOutHandler;
import org.pac4j.cas.client.CasClient;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.store.GuavaStore;
import org.pac4j.core.store.Store;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * This class is the logout handler for the {@link CasClient}, inspired by the {@link SingleSignOutHandler} of the Apereo CAS client.
 *
 * @author Jerome Leleu
 * @since 1.4.0
 */
public class DefaultCasLogoutHandler<C extends WebContext> implements CasLogoutHandler<C> {

    protected static final Logger logger = LoggerFactory.getLogger(DefaultCasLogoutHandler.class);

    private Store<String, Object> store = new GuavaStore<>(10000, 1, TimeUnit.HOURS);

    private boolean killSession;

    @Override
    public void recordSession(final C context, final String ticket) {
        final SessionStore sessionStore = context.getSessionStore();
        final String sessionId = sessionStore.getOrCreateSessionId(context);
        final Object trackableSession = sessionStore.getTrackableObject(context);

        logger.debug("ticket: {} -> trackableSession: {}", ticket, trackableSession);
        logger.debug("sessionId: {}", sessionId);
        store.set(ticket, trackableSession);
        store.set(sessionId, ticket);
    }

    @Override
    public void destroySessionFront(final C context, final String ticket) {
        store.remove(ticket);

        final SessionStore sessionStore = context.getSessionStore();
        final String currentSessionId = sessionStore.getOrCreateSessionId(context);
        logger.debug("currentSessionId: {}", currentSessionId);
        final String sessionToTicket = (String) store.get(currentSessionId);
        logger.debug("-> ticket: {}", ticket);
        store.remove(currentSessionId);

        if (CommonHelper.areEquals(ticket, sessionToTicket)) {
            destroy(context, sessionStore);
        } else {
            logger.error("Can not delete the pac4j profiles and optionally the web session for CAS front channel logout because the provided ticket is not the same as the one linked to the web session");
        }
    }

    protected void destroy(final C context, final SessionStore sessionStore) {
        // remove profiles
        final ProfileManager manager = new ProfileManager(context);
        manager.logout();
        // and optionally the web session
        if (killSession) {
            sessionStore.invalidateSession(context);
        }
    }

    @Override
    public void destroySessionBack(final C context, final String ticket) {
        final Object trackableObject = store.get(ticket);
        logger.debug("ticket: {} -> trackableObject: {}", ticket, trackableObject);
        if (trackableObject != null) {
            store.remove(ticket);

            // renew context with the original session store
            final SessionStore sessionStore = context.getSessionStore();
            final SessionStore newSesionStore = sessionStore.renewFromTrackableObject(context, trackableObject);
            logger.debug("newSesionStore: {}", newSesionStore);
            // can be null (backward compatibility for version 1.9) @Deprecated
            if (newSesionStore != null) {
                context.setSessionStore(newSesionStore);
                final String sessionId = newSesionStore.getOrCreateSessionId(context);
                logger.debug("remove sessionId: {}", sessionId);
                store.remove(sessionId);

                destroy(context, newSesionStore);
            } else {
                logger.error("sessionStore.renewFromTrackableObject returns null, ignoring for backward compatibility in version 1.9 of pac4j");
            }
        } else {
            logger.error("Can not delete the pac4j profiles and optionally the web session for CAS back channel logout because the tracked session is null");
        }
    }

    public Store<String, Object> getStore() {
        return store;
    }

    public void setStore(final Store<String, Object> store) {
        this.store = store;
    }

    public boolean isKillSession() {
        return killSession;
    }

    public void setKillSession(final boolean killSession) {
        this.killSession = killSession;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "store", store, "killSession", killSession);
    }
}
