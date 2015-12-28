package me.jcomo.smtpd.command;

public class StringUtils {
    public static boolean isBlank(String str) {
        return null == str || "".equals(str);
    }
}
