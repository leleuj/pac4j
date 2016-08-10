package org.pac4j.core.credentials.authenticator;

import com.google.common.cache.Cache;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.store.GuavaStore;
import org.pac4j.core.util.CommonHelper;

import java.util.concurrent.TimeUnit;

/**
 * Use {@link CachingAuthenticator} instead.
 *
 * @author Misagh Moayyed
 * @since 1.8
 * @deprecated
 */
@Deprecated
public class LocalCachingAuthenticator<T extends Credentials> extends CachingAuthenticator<T> {

    private long cacheSize;
    private long timeout;
    private TimeUnit timeUnit;

    public LocalCachingAuthenticator() {}

    public LocalCachingAuthenticator(final Authenticator<T> delegate, final long cacheSize,
                                     final long timeout, final TimeUnit timeUnit) {
        setDelegate(delegate);
        this.cacheSize = cacheSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotNull("delegate", getDelegate());
        CommonHelper.assertTrue(cacheSize > 0, "cacheSize must be > 0");
        CommonHelper.assertTrue(timeout > 0, "timeout must be > 0");
        CommonHelper.assertNotNull("timeUnit", this.timeUnit);

        setStore(new GuavaStore<>((int) cacheSize, (int) timeout, timeUnit));
    }

    protected Cache<T, CommonProfile> getGuavaCache() {
        return ((GuavaStore<T, CommonProfile>) getStore()).getCache();
    }

    public void removeFromCache(final T credentials) {
        getGuavaCache().invalidate(credentials);
    }

    public boolean isCached(final T credentials) {
        return getGuavaCache().getIfPresent(credentials) != null;
    }

    public boolean clearCache() {
        getGuavaCache().invalidateAll();
        return getGuavaCache().asMap().isEmpty();
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "delegate", getDelegate(), "cacheSize", this.cacheSize,
                "timeout", this.timeout, "timeUnit", this.timeUnit);
    }
}
