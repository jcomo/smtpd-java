package me.jcomo.smtpd;

import org.junit.Rule;
import org.junit.Test;

import java.util.NoSuchElementException;

import static me.jcomo.smtpd.TestHelper.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationTest {

    @Rule
    public final SystemProperties properties = new SystemProperties();

    @Test(expected = IllegalStateException.class)
    public void testRequiresFilesOrSystemPropertyPrefixesToBuild() throws Exception {
        new Configuration.Builder().build();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetThrowsWhenKeyDoesNotExist() throws Exception {
        final Configuration config = new Configuration.Builder()
                .withSystemProperties("test").build();

        config.get("missing.value");
    }

    @Test
    public void testGetWithDefaultFallsBackWhenKeyDoesNotExist() throws Exception {
        final Configuration config = new Configuration.Builder()
                .withSystemProperties("test").build();

        assertThat(config.get("missing.value", "thing")).isEqualTo("thing");
        assertThat(config.getInt("missing.number", 1)).isEqualTo(1);
    }

    @Test
    public void testReadsValuesFromFile() throws Exception {
        final Configuration config = new Configuration.Builder()
                .addValues(fixture("sample.properties"))
                .build();

        assertThat(config.get("sample.string")).isEqualTo("test");
    }

    @Test
    public void testReadsValuesFromFilesInOrder() throws Exception {
        final Configuration config = new Configuration.Builder()
                .addValues(fixture("sample.properties"))
                .addValues(fixture("override.properties"))
                .build();

        assertThat(config.get("sample.string")).isEqualTo("override");
        assertThat(config.getInt("sample.int")).isEqualTo(5);
    }

    @Test
    public void testReadsSystemPropertiesWithPrefix() throws Exception {
        properties.set("test.sample.string", "system");

        final Configuration config = new Configuration.Builder()
                .withSystemProperties("test")
                .build();

        assertThat(config.get("sample.string")).isEqualTo("system");
    }

    @Test
    public void testDoesNotReadSystemPropertiesWithoutPrefix() throws Exception {
        properties.set("sample.int", "10");
        properties.set("prefix.sample.int", "10");

        final Configuration config = new Configuration.Builder()
                .withSystemProperties("test")
                .build();

        assertThat(config.getInt("sample.int", 0)).isEqualTo(0);
        assertThat(config.getInt("prefix.sample.int", 0)).isEqualTo(0);
    }

    @Test
    public void testFavorsSystemPropertiesOverFileValues() throws Exception {
        properties.set("test.sample.string", "system");

        final Configuration config = new Configuration.Builder()
                .withSystemProperties("test")
                .addValues(fixture("sample.properties"))
                .build();

        assertThat(config.get("sample.string")).isEqualTo("system");
        assertThat(config.getInt("sample.int")).isEqualTo(5);
    }
}