package me.ericjiang.settlers.util;

public class Base62 {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int BASE = 62;

    public static String encode(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        encodeAndAppend(i, stringBuilder);
        return stringBuilder.toString();
    }

    public static String encode(long l) {
        int mostSignificant = (int) l >> 32;
        int leastSignificant = (int) l;
        StringBuilder stringBuilder = new StringBuilder();
        encodeAndAppend(mostSignificant, stringBuilder);
        encodeAndAppend(leastSignificant, stringBuilder);
        return stringBuilder.toString();
    }

    public static int toInt(String s) {
        int value = 0;
        for (int i = 0; i < s.length(); i++) {
            int multiplier = s.length() - i - 1;
            int part = ALPHABET.indexOf(s.charAt(i));
            for (int j = 0; j < multiplier; j++) {
                part *= BASE;
            }
            value += part;
        }
        return value;
    }

    public static long toLong(String s) {
        return 0L;
    }

    private static void encodeAndAppend(int i, StringBuilder sb) {
        if (i < BASE) {
            sb.append(ALPHABET.charAt(i));
        } else {
            encodeAndAppend(i / BASE, sb);
            encodeAndAppend(i % BASE, sb);
        }
    }

}