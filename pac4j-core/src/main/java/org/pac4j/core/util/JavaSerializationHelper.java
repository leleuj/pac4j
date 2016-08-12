package org.pac4j.core.util;

import org.pac4j.core.store.serializer.JavaSerializer;

import java.io.*;

/**
 * Use {@link JavaSerializer} instead.
 *
 * @author Jerome Leleu
 * @since 1.8.1
 * @deprecated
 */
@Deprecated
public class JavaSerializationHelper {

    private static final JavaSerializer serializer = new JavaSerializer();

    /**
     * Serialize a Java object into a base64 String.
     *
     * @param o the object to serialize
     * @return the base64 string of the serialized object
     */
    public String serializeToBase64(final Serializable o) {
        return serializer.serializeToBase64(o);
    }

    /**
     * Serialize a Java object into a bytes array.
     *
     * @param o the object to serialize
     * @return the bytes array of the serialized object
     */
    public byte[] serializeToBytes(final Serializable o) {
        return serializer.serialize(o);
    }

    /**
     * Unserialize a base64 String into a Java object.
     *
     * @param base64 the serialized object as a base64 String
     * @return the unserialized Java object
     */
    public Serializable unserializeFromBase64(final String base64) {
        return (Serializable) serializer.deserializeFromBase64(base64);
    }

    /**
     * Unserialize a bytes array into a Java object.
     *
     * @param bytes the serialized object as a bytes array
     * @return the unserialized Java object
     */
    public Serializable unserializeFromBytes(final byte[] bytes) {
        return (Serializable) serializer.deserialize(bytes);
    }
}
