package org.pac4j.core.store.serializer;

import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.Transcoder;
import org.pac4j.core.util.CommonHelper;

import java.io.Serializable;

/**
 * Transcoder to wrap a {@link Serializer}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class SerializerTranscoder<T extends Serializable> implements Transcoder<T> {

    private final Serializer<T> serializer;

    public SerializerTranscoder(final Serializer<T> serializer) {
        this.serializer = serializer;
    }

    @Override
    public boolean asyncDecode(final CachedData data) {
        return false;
    }

    @Override
    public CachedData encode(final T obj) {
        final byte[] bytes = serializer.serialize(obj);
        return new CachedData(0, bytes, bytes.length);
    }

    @Override
    public T decode(final CachedData data) {
        final byte[] bytes = data.getData();
        return serializer.deserialize(bytes);
    }

    @Override
    public int getMaxSize() {
        return CachedData.MAX_SIZE;
    }

    public Serializer<T> getSerializer() {
        return serializer;
    }

    @Override
    public String toString() {
        return CommonHelper.toString(this.getClass(), "serializer", serializer);
    }
}
