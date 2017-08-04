package me.ericjiang.settlers.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class Base62Test {

    @Test
    public void shouldEncodeLessThan62() {
        assertEquals("0", Base62.encode(0));
        assertEquals("a", Base62.encode(10));
        assertEquals("Z", Base62.encode(61));
    }

    @Test
    public void shouldEncodeMoreThan62() {
        assertEquals("10", Base62.encode(62));
    }

    @Test
    public void shouldEncodeMoreThan62Squared() {
        assertEquals("100", Base62.encode(3844));
    }

    @Test
    public void shouldEncodeAndDecode() {
        assertEquals(0, Base62.toInt(Base62.encode(0)));
        assertEquals(10, Base62.toInt(Base62.encode(10)));
        assertEquals(62, Base62.toInt(Base62.encode(62)));
        assertEquals(3844, Base62.toInt(Base62.encode(3844)));
    }

    @Test
    public void shouldEncodeLongs() {
        System.out.println(Base62.encode(Integer.MAX_VALUE));
        assertEquals("00", Base62.encode(0L));
    }

}