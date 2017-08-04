package me.ericjiang.settlers.util;

import java.nio.ByteBuffer;
import java.util.UUID;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ShortUUID {

    private final UUID uuid;

    private static final BaseEncoding BASE_ENCODING = BaseEncoding.base64Url().omitPadding();

    public ShortUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public ShortUUID(long mostSignificantBits, long leastSignificantBits) {
        this(new UUID(mostSignificantBits, leastSignificantBits));
    }

    public static ShortUUID randomUUID() {
        return new ShortUUID(UUID.randomUUID());
    }

    public static ShortUUID valueOf(String shortUUID) {
        byte[] bytes = BASE_ENCODING.decode(shortUUID);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long mostSignificantBits = buffer.getLong();
        long leastSignificantBits = buffer.getLong();
        return new ShortUUID(mostSignificantBits, leastSignificantBits);
    }

    @Override
    public String toString() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * 2);
        buffer.put(Longs.toByteArray(uuid.getMostSignificantBits()));
        buffer.put(Longs.toByteArray(uuid.getLeastSignificantBits()));
        return BASE_ENCODING.encode(buffer.array());
    }

    public long getMostSignificantBits() {
        return uuid.getMostSignificantBits();
    }

    public long getLeastSignificantBits() {
        return uuid.getLeastSignificantBits();
    }

    public UUID asUUID() {
        return uuid;
    }

}