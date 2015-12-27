package me.jcomo.smtpd;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Session {
    private PrintWriter output;
    private BufferedReader input;
    private Envelope envelope;

    public Session(PrintWriter output, BufferedReader input) {
        this.output = output;
        this.input = input;
    }

    public void sendResponse(String response) {
        output.println(response);
        output.flush();
    }

    public void setSender(String sender) {
        envelope = new Envelope(sender);
    }

    public void addRecipient(String recipient) {
        envelope.addRecipient(recipient);
    }
}
