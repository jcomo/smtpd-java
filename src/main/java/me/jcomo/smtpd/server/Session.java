package me.jcomo.smtpd.server;

import me.jcomo.smtpd.message.MessageBuffer;

import java.io.PrintWriter;

public class Session {
    private String hostname;
    private String domain;
    private final PrintWriter output;
    private final MessageBuffer messageBuffer;

    public Session(String hostname, PrintWriter output, MessageBuffer messageBuffer) {
        this.hostname = hostname;
        this.output = output;
        this.messageBuffer = messageBuffer;
    }

    public void sendReply(Reply reply) {
        output.print(reply.render());
        output.flush();
    }

    public String getHostname() {
        return hostname;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public MessageBuffer getMessageBuffer() {
        return messageBuffer;
    }
}
