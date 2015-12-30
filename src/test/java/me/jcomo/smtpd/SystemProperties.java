package me.jcomo.smtpd;

import org.junit.rules.ExternalResource;

import java.util.HashSet;
import java.util.Set;

public class SystemProperties extends ExternalResource {
    private final Set<String> changedKeys = new HashSet<>();

    public void set(String key, String value) {
        changedKeys.add(key);
        System.setProperty(key, value);
    }

    @Override
    protected void after() {
        changedKeys.forEach(System::clearProperty);
    }
}

