package me.jcomo.smtpd.mailer;

import me.jcomo.smtpd.Configuration;

import java.io.File;
import java.io.PrintWriter;

public class MailerFactory {
    private final Configuration config;

    public MailerFactory(Configuration config) {
        this.config = config;
    }

    public static Mailer mailerFromConfiguration(Configuration config) {
        return new MailerFactory(config).createMailer();
    }

    public Mailer createMailer() {
        String mailer = config.get("mailer.type", "debug").toLowerCase();

        switch (mailer) {
            case "debug":
                return createDebugMailer();
            case "file":
                return createFileMailer();
            default:
                throw new IllegalArgumentException("Unrecognized mailer type: " + mailer);
        }
    }

    private Mailer createDebugMailer() {
        return new DebugMailer(new PrintWriter(System.out));
    }

    private Mailer createFileMailer() {
        String mailDirName = config.get("mailer.file.directory", "/tmp/mail");
        File mailDir = createDirectoryOrFail(mailDirName);
        FileFactory factory = new FileFactory(mailDir);
        return new FileMailer(factory);
    }

    private File createDirectoryOrFail(String dirName) {
        File mailDir = new File(dirName);
        if (!mailDir.exists()) {
            boolean success = mailDir.mkdirs();
            if (!success) {
                throw new RuntimeException("Could not create directory: " + dirName);
            }
        } else if (!mailDir.isDirectory()) {
            throw new RuntimeException(dirName + " already exists and is not a directory");
        }

        return mailDir;
    }
}
