package org.pac4j.core.store.serializer;

import javax.xml.bind.DatatypeConverter;

/**
 * The serializer interface.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public interface Serializer<T> {

    /**
     * Serialize a Java object into a base64 String.
     *
     * @param obj the object
     * @return the base64 String
     */
    default String serializeToBase64(final T obj) {
        return DatatypeConverter.printBase64Binary(serialize(obj));
    }

    /**
     * Serialize a Java object into a bytes array.
     *
     * @param obj the object
     * @return the bytes array
     */
    byte[] serialize(T obj);

    /**
     * Deserialize a base64 String array into an object.
     *
     * @param base64 the base64 String
     * @return the object
     */
    default T deserializeFromBase64(final String base64) {
        return deserialize(DatatypeConverter.parseBase64Binary(base64));
    }

    /**
     * Deserialize a bytes array into an object.
     *
     * @param bytes the bytes array
     * @return the object
     */
    T deserialize(byte[] bytes);
}
