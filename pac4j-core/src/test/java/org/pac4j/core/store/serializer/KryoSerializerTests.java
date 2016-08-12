package org.pac4j.core.store.serializer;

/**
 * Tests {@link KryoSerializer}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public final class KryoSerializerTests extends AbstractSerializerTests {

    @Override
    protected Serializer getSerializer() {
        return new KryoSerializer();
    }
}
