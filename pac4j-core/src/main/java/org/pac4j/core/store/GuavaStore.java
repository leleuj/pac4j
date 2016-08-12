package org.pac4j.core.store;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.InitializableObject;

import java.util.concurrent.TimeUnit;

/**
 * Store data in a Guava cache.
 *
 * Add the <code>guava</code> dependency to use this store.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class GuavaStore<K, O> extends InitializableObject implements Store<K, O> {

    private Cache<K, O> cache;

    private int size = 0;

    private int timeout = -1;

    private TimeUnit timeUnit;

    public GuavaStore() {}

    public GuavaStore(final int size, final int timeout, final TimeUnit timeUnit) {
        this.size = size;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    protected void internalInit() {
        CommonHelper.assertTrue(this.size > 0, "size mut be greater than zero");
        CommonHelper.assertTrue(this.timeout >= 0, "timeout must be greater than zero");
        CommonHelper.assertNotNull("timeUnit", this.timeUnit);

        this.cache = CacheBuilder.newBuilder().maximumSize(this.size)
                .expireAfterWrite(this.timeout, this.timeUnit).build();
    }

    @Override
    public O get(final K key) {
        init();

        if (key != null) {
            return cache.getIfPresent(key);
        }
        return null;
    }

    @Override
    public void set(final K key, final O value) {
        init();
        CommonHelper.assertNotNull("value", value);

        if (key != null) {
            cache.put(key, value);
        }
    }

    @Override
    public void remove(final K key) {
        init();
        if (key != null) {
            cache.invalidate(key);
        }
    }

    public Cache<K, O> getCache() {
        return cache;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(final TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "size", size, "timeout", timeout, "timeUnit", timeUnit);
    }
}
