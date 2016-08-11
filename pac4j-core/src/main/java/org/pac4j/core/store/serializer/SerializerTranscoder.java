package org.pac4j.core.store.serializer;

import net.spy.memcached.CachedData;
import net.spy.memcached.transcoders.Transcoder;

public class SerializerTranscoder<T> implements Transcoder<T> {

    private Serializer<T> serializer;

    @Override
    public boolean asyncDecode(final CachedData data) {
        return false;
    }

    @Override
    public CachedData encode(T obj) {
        final byte[] bytes = serializer.serialize(obj);
        return new CachedData(0, bytes, bytes.length);
    }

    @Override
    public T decode(CachedData var1) {
        return null;
    }

    @Override
    public int getMaxSize() {
        return CachedData.MAX_SIZE;
    }
}
