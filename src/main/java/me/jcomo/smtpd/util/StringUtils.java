package me.jcomo.smtpd.util;

public class StringUtils {
    public static boolean isBlank(String str) {
        return null == str || "".equals(str);
    }
}
