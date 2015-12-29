package me.jcomo.smtpd.util;

import org.junit.Test;

import java.util.Optional;

import static me.jcomo.smtpd.util.EmailUtils.parseEmailAddress;
import static org.assertj.core.api.Assertions.assertThat;

public class EmailUtilsTest {

    @Test
    public void testParsesValidEmail() throws Exception {
        Optional<String> email = parseEmailAddress("<test@example.com>");
        assertThat(email).contains("test@example.com");
    }

    @Test
    public void testParsesEmailWithWhitespaceBetweenBrackets() throws Exception {
        Optional<String> email = parseEmailAddress("<   test@example.com >");
        assertThat(email).contains("test@example.com");
    }

    @Test
    public void testDoesNotParseEmailWithoutBrackets() throws Exception {
        Optional<String> email = parseEmailAddress("test@example.com");
        assertThat(email).isEmpty();
    }

    @Test
    public void testDoesNotParseInvalidEmail() throws Exception {
        Optional<String> email = parseEmailAddress("mail.com");
        assertThat(email).isEmpty();
    }
}