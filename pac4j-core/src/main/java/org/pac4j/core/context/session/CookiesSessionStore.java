package org.pac4j.core.context.session;

import org.pac4j.core.context.ContextHelper;
import org.pac4j.core.context.Cookie;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.store.serializer.JavaSerializer;
import org.pac4j.core.store.serializer.Serializer;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.InitializableWebObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Session store which saves data into cookies: session tracking and renewal are not supported.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class CookiesSessionStore<C extends WebContext> extends InitializableWebObject implements SessionStore<C> {

    private final static Logger logger = LoggerFactory.getLogger(CookiesSessionStore.class);

    public final static String DEFAULT_COOKIE_PREFIX = "pac4jCookies_";

    private String prefix = DEFAULT_COOKIE_PREFIX;

    private Serializer serializer = new JavaSerializer<>();

    private String domain;

    private boolean httpOnly;

    private boolean secure;

    private String path;

    private int maxAge = -1;

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotBlank("prefix", prefix);
        CommonHelper.assertNotNull("serializer", serializer);

        if (CommonHelper.isBlank(domain)) {
            domain = context.getServerName();
        }
    }

    @Override
    public String getOrCreateSessionId(final C context) {
        return null;
    }

    @Override
    public Object get(final C context, final String key) {
        init(context);

        final String name = prefix + key;
        final Cookie cookie = ContextHelper.getCookie(context, name);
        if (cookie != null) {
            final String value = cookie.getValue();
            logger.debug("Reading cookie: {}, value: {}", name, value);
            final Object obj = serializer.deserializeFromBase64(value);
            logger.debug("Deserializing: {}", obj);
            return obj;
        }
        return null;
    }

    @Override
    public void set(final C context, final String key, final Object value) {
        init(context);

        if (value != null) {
            logger.debug("Serializing: {}", value);
            final String name = prefix + key;
            final String serializedValue = serializer.serializeToBase64(value);
            logger.debug("Writing cookie: {}, value: {}", name, serializedValue);
            final Cookie cookie = buildCookie(name, serializedValue, maxAge);
            context.addResponseCookie(cookie);
        }
    }

    @Override
    public boolean invalidateSession(final C context) {
        init(context);

        final List<String> cookieNames = new ArrayList<>();
        final Collection<Cookie> cookies = context.getRequestCookies();
        for (final Cookie cookie: cookies) {
            final String name = cookie.getName();
            if (name != null && name.startsWith(prefix)) {
                cookieNames.add(name);
            }
        }
        for (final String name: cookieNames) {
            context.addResponseCookie(buildCookie(name, "", 0));
        }
        // if a cookie has been removed, we consider that the invalidation works
        return cookieNames.size() > 0;
    }

    protected Cookie buildCookie(final String name, final String value, final int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        if (CommonHelper.isNotBlank(path)) {
            cookie.setPath(path);
        }
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(final Serializer serializer) {
        this.serializer = serializer;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(final boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(final boolean secure) {
        this.secure = secure;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(final int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "prefix", prefix, "serializer", serializer,
                "domain", domain, "httpOnly", httpOnly, "secure", secure, "path", path, "maxAge", maxAge);
    }
}
