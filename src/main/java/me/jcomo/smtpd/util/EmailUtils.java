package me.jcomo.smtpd.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.jcomo.smtpd.util.StringUtils.isBlank;

public class EmailUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("<\\s*(\\S+)\\s*>");

    public static Optional<String> parseEmailAddress(String str) {
        Matcher m = EMAIL_PATTERN.matcher(str);
        if (m.find()) {
            String email = m.group(1);
            if (isValidEmail(email)) {
                return Optional.of(email);
            }
        }

        return Optional.empty();
    }

    private static boolean isValidEmail(String email) {
        return !isBlank(email) && email.indexOf('@') > 0;
    }
}
