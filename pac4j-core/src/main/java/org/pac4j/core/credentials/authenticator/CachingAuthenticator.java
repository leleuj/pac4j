package org.pac4j.core.credentials.authenticator;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.store.Store;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.InitializableWebObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An authenticator that caches the result of an authentication (user profile) in a given store.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class CachingAuthenticator<T extends Credentials> extends InitializableWebObject implements Authenticator<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Authenticator<T> delegate;

    private Store<T, CommonProfile> store;

    public CachingAuthenticator() {}

    public CachingAuthenticator(final Authenticator<T> delegate, final Store<T, CommonProfile> store) {
        this.delegate = delegate;
        this.store = store;
    }

    @Override
    public void validate(final T credentials, final WebContext context) throws HttpAction {
        init(context);

        CommonProfile profile = this.store.get(credentials);
        if (profile == null) {
            logger.debug("Delegating authentication to {}...", delegate);
            delegate.validate(credentials, context);
            profile = credentials.getUserProfile();
            this.store.set(credentials, profile);
        }
        credentials.setUserProfile(profile);
        logger.debug("Found cached credential. Using cached profile {}...", profile);
    }

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotNull("delegate", this.delegate);
    }

    public Authenticator<T> getDelegate() {
        return delegate;
    }

    public void setDelegate(final Authenticator<T> delegate) {
        this.delegate = delegate;
    }

    public Store<T, CommonProfile> getStore() {
        return store;
    }

    public void setStore(final Store<T, CommonProfile> store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "delegate", this.delegate, "store", this.store);
    }
}
