package org.pac4j.core.store.serializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * The Java serializer.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public class JavaSerializer<T extends Serializable> implements Serializer<T> {

    private static final Logger logger = LoggerFactory.getLogger(JavaSerializer.class);

    @Override
    public byte[] serialize(final T obj) {
        byte[] bytes = null;
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
             final ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            bytes = baos.toByteArray();
        } catch (final IOException e) {
            logger.warn("cannot Java serialize object", e);
        }
        return bytes;
    }

    @Override
    public T deserialize(final byte[] bytes) {
        T o = null;
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             final ObjectInputStream ois = new ObjectInputStream(bais)) {
            o = (T) ois.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            logger.warn("cannot Java deserialize object", e);
        }
        return o;
    }
}
