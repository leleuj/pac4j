package org.pac4j.core.store.serializer;

/**
 * The serializer interface.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public interface Serializer<T> {

    /**
     * Serialize a Java object into a bytes array.
     *
     * @param obj the object
     * @return the bytes array
     */
    byte[] serialize(T obj);
}
