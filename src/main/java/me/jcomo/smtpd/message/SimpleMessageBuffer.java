package me.jcomo.smtpd.message;

import me.jcomo.smtpd.mailer.Mailer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleMessageBuffer implements MessageBuffer {
    private String sender;
    private final List<String> recipients = new ArrayList<>();

    private final BufferedReader buf;
    private final StringBuilder builder = new StringBuilder();

    private final Mailer mailer;

    public SimpleMessageBuffer(BufferedReader buf, Mailer mailer) {
        this.buf = buf;
        this.mailer = mailer;
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public void addRecipient(String recipient) {
        recipients.add(recipient);
    }

    @Override
    public void receiveData() {
        String line;
        try {
            while ((line = buf.readLine()) != null) {
                if (endOfMessage(line)) {
                    removeTrailingNewline();
                    break;
                }

                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean endOfMessage(String line) {
        return ".".equals(line);
    }

    private void removeTrailingNewline() {
        builder.setLength(builder.length() - 1);
    }

    public void reset() {
        sender = null;
        recipients.clear();
        builder.setLength(0);
    }

    @Override
    public void done() {
        String mailBody = builder.toString();
        for (String recipient : recipients) {
            mailer.send(sender, recipient, mailBody);
        }

        reset();
    }
}
