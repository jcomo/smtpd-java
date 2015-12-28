package me.jcomo.smtpd.mailer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileBlobFactory {
    public final String baseDir;

    public FileBlobFactory(String baseDir) {
        this.baseDir = baseDir;
    }

    public File createFile() {
        File newFile = new File(baseDir, randomId());
        try {
            boolean successful = newFile.createNewFile();
            if (!successful) {
                throw new RuntimeException("Failed to create file at: " + newFile.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newFile;
    }

    private String randomId() {
        return UUID.randomUUID().toString();
    }
}
