package org.pac4j.core.store;

import net.spy.memcached.MemcachedClientIF;
import net.spy.memcached.transcoders.Transcoder;
import org.pac4j.core.store.serializer.JavaSerializer;
import org.pac4j.core.store.serializer.Serializer;
import org.pac4j.core.store.serializer.SerializerTranscoder;
import org.pac4j.core.util.CommonHelper;

/**
 * Store data in Memcached.
 *
 * Add the <code>kryo-shaded</code> dependency to use this store.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class MemcachedStore<K, O> extends AbstractMemoryStore<K, O> {

    private MemcachedClientIF client;

    private Serializer serializer = new JavaSerializer<>();

    private Transcoder<O> transcoder;

    private int timeoutSeconds = 0;

    public MemcachedStore() {}

    public MemcachedStore(final MemcachedClientIF client, final int timeoutSeconds) {
        this.client = client;
        this.timeoutSeconds = timeoutSeconds;
    }

    public MemcachedStore(final MemcachedClientIF client, final Serializer serializer, final int timeoutSeconds) {
        this.client = client;
        this.serializer = serializer;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    protected void internalInit() {
        CommonHelper.assertNotNull("client", this.client);
        CommonHelper.assertNotNull("serializer", this.serializer);
        CommonHelper.assertTrue(timeoutSeconds > 0, "timeoutSeconds must be greater than zero");

        this.transcoder = new SerializerTranscoder<>(this.serializer);
    }

    @Override
    protected O internalGet(final K key) {
        return client.get(serializer.serializeToBase64(key), this.transcoder);
    }

    @Override
    protected void internalSet(final K key, final O value) {
        client.set(serializer.serializeToBase64(key), timeoutSeconds, value, this.transcoder);
    }

    @Override
    protected void internalRemove(final K key) {
        client.delete(serializer.serializeToBase64(key));
    }

    public MemcachedClientIF getClient() {
        return client;
    }

    public void setClient(final MemcachedClientIF client) {
        this.client = client;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(final Serializer serializer) {
        this.serializer = serializer;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(final int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public MemcachedClientIF getCache() {
        return this.client;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "client", client, "serializer", serializer, "timeoutSeconds", timeoutSeconds);
    }
}
