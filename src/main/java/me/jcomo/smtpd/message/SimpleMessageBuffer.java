package me.jcomo.smtpd.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleMessageBuffer implements MessageBuffer {
    private String sender;
    private final List<String> recipients = new ArrayList<>();
    private final BufferedReader buf;

    public SimpleMessageBuffer(BufferedReader buf) {
        this.buf = buf;
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
                System.out.println(line);
                if (".".equals(line)) {
                    System.out.println("DONE");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void done() {

    }
}
