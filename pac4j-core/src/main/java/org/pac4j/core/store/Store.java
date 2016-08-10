package org.pac4j.core.store;

import java.io.Serializable;

/**
 * Store data.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public interface Store<K extends Serializable, O extends Serializable> {

    /**
     * Get a value by key.
     *
     * @param key the key
     * @return the object
     */
    O get(K key);

    /**
     * Set a value by its key.
     *
     * @param key the key
     * @param value the value
     */
    void set(K key, O value);
}
