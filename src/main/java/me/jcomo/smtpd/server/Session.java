package me.jcomo.smtpd.server;

import me.jcomo.smtpd.message.MessageBuffer;

import java.io.PrintWriter;

public class Session {
    private String domain;
    private final PrintWriter output;
    private final MessageBuffer messageBuffer;

    public Session(PrintWriter output, MessageBuffer messageBuffer) {
        this.output = output;
        this.messageBuffer = messageBuffer;
    }

    public void sendReply(Reply reply) {
        output.print(reply.render());
        output.flush();
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public MessageBuffer getMessageBuffer() {
        return messageBuffer;
    }
}
