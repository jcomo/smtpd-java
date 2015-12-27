package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Session;

public class RcptCommand implements Command {
    private static final String RCPT_TO = "RCPT TO:";

    private Session session;
    private String line;

    public RcptCommand(Session session, String line) {
        this.session = session;
        this.line = line;
    }

    @Override
    public boolean execute() {
        String recipient = getRecipient();
        if (null == recipient) {
            session.sendResponse("440 bad to");
            return false;
        }

        session.addRecipient(recipient);
        session.sendResponse("250 OK to " + recipient);
        return true;
    }

    private String getRecipient() {
        if (line.startsWith(RCPT_TO)) {
            String recipient = line.substring(RCPT_TO.length());
            if (!"".equals(recipient)) {
                return recipient;
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "RCPT";
    }
}
