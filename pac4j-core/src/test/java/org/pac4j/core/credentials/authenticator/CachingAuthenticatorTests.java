package org.pac4j.core.credentials.authenticator;

import org.junit.Test;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.store.InMemoryStore;
import org.pac4j.core.util.TestsConstants;

import static org.junit.Assert.*;

/**
 * Test cases for {@link CachingAuthenticator}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class CachingAuthenticatorTests implements TestsConstants {

    private static class NumberedAuthenticator implements Authenticator<UsernamePasswordCredentials> {

        private int n = 0;

        @Override
        public void validate(final UsernamePasswordCredentials credentials, final WebContext context) {
            final CommonProfile profile = new CommonProfile();
            profile.setId("" + n);
            credentials.setUserProfile(profile);
            n++;
        }
    }

    @Test
    public void testCacheWorks() throws HttpAction {
        final NumberedAuthenticator authenticator = new NumberedAuthenticator();
        final InMemoryStore store = new InMemoryStore();
        final CachingAuthenticator cachingAuthenticator = new CachingAuthenticator(authenticator, store);
        final Credentials credentials1 = new UsernamePasswordCredentials("0", "00", this.getClass().getName());
        cachingAuthenticator.validate(credentials1, null);
        final String id1 = credentials1.getUserProfile().getId();
        final Credentials credentials2 = new UsernamePasswordCredentials("0", "00", this.getClass().getName());
        cachingAuthenticator.validate(credentials2, null);
        final String id2 = credentials2.getUserProfile().getId();
        assertEquals(id1, id2);
    }

    @Test
    public void testWithoutCache() throws HttpAction {
        final NumberedAuthenticator authenticator = new NumberedAuthenticator();
        final InMemoryStore store = new InMemoryStore();
        final CachingAuthenticator cachingAuthenticator = new CachingAuthenticator(authenticator, store);
        final Credentials credentials1 = new UsernamePasswordCredentials("a", "a", this.getClass().getName());
        cachingAuthenticator.validate(credentials1, null);
        final String id1 = credentials1.getUserProfile().getId();
        final Credentials credentials2 = new UsernamePasswordCredentials("a", "b", this.getClass().getName());
        cachingAuthenticator.validate(credentials2, null);
        final String id2 = credentials2.getUserProfile().getId();
        assertEquals("0", id1);
        assertEquals("1", id2);
    }
}
