package me.jcomo.smtpd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Configuration {
    private final Properties props;

    @SuppressWarnings("unused")
    private Configuration() {
        this(new Properties());
    }

    private Configuration(Properties props) {
        this.props = props;
    }

    public String get(String key) {
        return getOpt(key, Function.identity());
    }

    public String get(String key, String defaultValue) {
        return getDefaultOpt(key, defaultValue, Function.identity());
    }

    public int getInt(String key) {
        return getOpt(key, Integer::parseInt);
    }

    public int getInt(String key, int defaultValue) {
        return getDefaultOpt(key, defaultValue, Integer::parseInt);
    }

    private <T> T getOpt(String key, Function<String, T> f) {
        return Optional.ofNullable(props.getProperty(key)).map(f).orElseThrow(NoSuchElementException::new);
    }

    private <T> T getDefaultOpt(String key, T defaultValue, Function<String, T> f) {
        return Optional.ofNullable(props.getProperty(key)).map(f).orElse(defaultValue);
    }

    public static class Builder {
        private final List<File> files = new ArrayList<>();
        private final List<String> propertyPrefixes = new ArrayList<>();

        public Builder addValues(File file) {
            files.add(file);
            return this;
        }

        public Builder withSystemProperties(String prefix) {
            propertyPrefixes.add(prefix + ".");
            return this;
        }

        public Configuration build() throws IOException {
            if (files.isEmpty() && propertyPrefixes.isEmpty()) {
                throw new IllegalStateException("Must supply at least one file"
                        + " or one system property prefix to build configuration");
            }
            Properties props = new Properties();
            for (File file : files) {
                props.load(new FileInputStream(file));
            }

            Properties systemProps = System.getProperties();
            for (String prefix : propertyPrefixes) {
                Enumeration iter = systemProps.propertyNames();
                while (iter.hasMoreElements()) {
                    String key = (String) iter.nextElement();
                    if (key.startsWith(prefix)) {
                        String normalKey = key.substring(prefix.length());
                        props.put(normalKey, systemProps.getProperty(key));
                    }
                }
            }

            return new Configuration(props);
        }
    }
}
