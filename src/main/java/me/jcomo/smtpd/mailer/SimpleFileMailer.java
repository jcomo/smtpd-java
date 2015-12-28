package me.jcomo.smtpd.mailer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SimpleFileMailer implements Mailer {
    private final FileBlobFactory fileFactory;

    public SimpleFileMailer(FileBlobFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    @Override
    public void send(String from, String to, String data) {
        File outputFile = fileFactory.createFile();
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println("From: " + from);
            writer.println("To: " + to);
            writer.println();
            writer.println(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
