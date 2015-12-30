package me.jcomo.smtpd;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Fail.fail;

public class TestHelper {
    public static File fixture(String name) {
        URL resource = TestHelper.class.getClassLoader().getResource(name);
        if (null == resource) {
            fail("No resource found matching: " + name);
        }

        return new File(resource.getFile());
    }
}
