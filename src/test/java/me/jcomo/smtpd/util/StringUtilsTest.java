package me.jcomo.smtpd.util;

import org.junit.Test;

import static me.jcomo.smtpd.util.StringUtils.isBlank;
import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilsTest {

    @Test
    public void testNullStringIsBlank() throws Exception {
        assertThat(isBlank(null)).isTrue();
    }

    @Test
    public void testBlankStringIsBlank() throws Exception {
        assertThat(isBlank("")).isTrue();
    }

    @Test
    public void testStringWithContentsIsNotBlank() throws Exception {
        assertThat(isBlank("a")).isFalse();
        assertThat(isBlank("thing")).isFalse();
    }
}