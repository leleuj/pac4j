package org.pac4j.core.store.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;

/**
 * The Kryo serializer.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class KryoSerializer<T extends Serializable> implements Serializer<T> {

    private final Kryo kryo;

    public KryoSerializer() {
        this.kryo = new Kryo();
    }

    public KryoSerializer(final Kryo kryo) {
        this.kryo = kryo;
    }

    @Override
    public byte[] serialize(final T obj) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (final Output output = new Output(byteStream)) {
            kryo.writeClassAndObject(output, obj);
            output.flush();
            return byteStream.toByteArray();
        }
    }

    @Override
    public T deserialize(final byte[] bytes) {
        try (final Input input = new Input(new ByteArrayInputStream(bytes))) {
            final T obj =  (T) kryo.readClassAndObject(input);
            return obj;
        }
    }
}
