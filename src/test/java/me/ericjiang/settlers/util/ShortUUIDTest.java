package me.ericjiang.settlers.util;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

public class ShortUUIDTest {

    @Test
    public void shouldDecodeWhatWeEncode() {
        ShortUUID uuid = ShortUUID.randomUUID();
        ShortUUID uuidCopy = ShortUUID.valueOf(uuid.toString());
        assertEquals(uuid.getMostSignificantBits(), uuidCopy.getMostSignificantBits());
        assertEquals(uuid.getLeastSignificantBits(), uuidCopy.getLeastSignificantBits());
    }

    @Test
    public void shouldCompareBits() {
        UUID uuid = UUID.randomUUID();
        UUID uuidCopy = new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
        assertEquals(new ShortUUID(uuid), new ShortUUID(uuidCopy));
    }

}