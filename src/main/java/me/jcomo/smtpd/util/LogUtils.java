package me.jcomo.smtpd.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LogUtils {
    public static String formatStackTrace(Throwable e) {
        String trace = Arrays.stream(e.getStackTrace())
                .map(line -> " ! " + line)
                .collect(Collectors.joining("\n"));

        return "Uncaught exception occurred. Stack trace below\n" + trace;
    }
}
