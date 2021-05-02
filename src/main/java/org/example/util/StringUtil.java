package org.example.util;

public class StringUtil {
    public static final String EMPTY = "";

    public static boolean isNotBlank(String s) {
        return s != null && s.trim().length() > 0;
    }
}
