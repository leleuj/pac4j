package org.pac4j.core.store.serializer;

/**
 * Tests {@link JavaSerializer}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class JavaSerializerTests extends AbstractSerializerTests {

    @Override
    protected Serializer getSerializer() {
        return new JavaSerializer();
    }
}
