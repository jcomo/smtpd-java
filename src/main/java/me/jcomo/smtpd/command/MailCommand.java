package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Session;

public class MailCommand implements Command {
    private static final String MAIL_FROM = "MAIL FROM:";

    private Session session;
    private String line;

    public MailCommand(Session session, String line) {
        this.session = session;
        this.line = line;
    }

    @Override
    public boolean execute() {
        String sender = getSender();
        if (null == sender) {
            // TODO: whats the right error code here?
            session.sendResponse("440 Bad from");
            return false;
        }

        session.setSender(sender);
        session.sendResponse("250 OK from " + sender);
        return true;
    }

    private String getSender() {
        if (line.startsWith(MAIL_FROM)) {
            String sender = line.substring(MAIL_FROM.length());
            if (!"".equals(sender)) {
                return sender;
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "MAIL";
    }
}
