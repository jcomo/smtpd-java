package me.jcomo.smtpd;

import java.util.ArrayList;
import java.util.List;

public class Envelope {
    private String sender;
    private List<String> recipients = new ArrayList<>();

    public Envelope(String sender) {
        this.sender = sender;
    }

    public void addRecipient(String recipient) {
        recipients.add(recipient);
    }
}
