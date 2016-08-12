package org.pac4j.core.context.session;

import org.pac4j.core.context.J2EContext;

import javax.servlet.http.HttpSession;

/**
 * Store data in the provided J2E session (not the one found in the context).
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class J2EProvidedSessionStore extends J2ESessionStore {

    private final HttpSession session;

    public J2EProvidedSessionStore(final HttpSession session) {
        this.session = session;
    }

    protected HttpSession getHttpSession(final J2EContext context) {
        return session;
    }
}
