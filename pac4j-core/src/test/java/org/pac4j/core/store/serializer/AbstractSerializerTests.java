package org.pac4j.core.store.serializer;

import org.junit.Test;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.TestsConstants;

import static org.junit.Assert.assertEquals;

/**
 * Abstract serializer tests.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
public abstract class AbstractSerializerTests implements TestsConstants {

    protected abstract Serializer getSerializer();

    private CommonProfile getUserProfile() {
        final CommonProfile profile = new CommonProfile();
        profile.setId(ID);
        profile.addAttribute(NAME, VALUE);
        return profile;
    }

    @Test
    public void testBytesSerialization() {
        final CommonProfile profile = getUserProfile();
        final byte[] serialized = getSerializer().serialize(profile);
        final CommonProfile profile2 = (CommonProfile) getSerializer().deserialize(serialized);
        assertEquals(profile.getId(), profile2.getId());
        assertEquals(profile.getAttribute(NAME), profile2.getAttribute(NAME));
    }

    @Test
    public void testBase64StringSerialization() {
        final CommonProfile profile = getUserProfile();
        final String serialized = getSerializer().serializeToBase64(profile);
        final CommonProfile profile2 = (CommonProfile) getSerializer().deserializeFromBase64(serialized);
        assertEquals(profile.getId(), profile2.getId());
        assertEquals(profile.getAttribute(NAME), profile2.getAttribute(NAME));
    }
}
