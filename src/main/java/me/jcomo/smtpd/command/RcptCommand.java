package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Reply;
import me.jcomo.smtpd.ReplyCode;
import me.jcomo.smtpd.Session;

import static me.jcomo.smtpd.command.StringUtils.isBlank;

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
            session.sendReply(new Reply(ReplyCode.SYNTAX_ERROR, "Syntax: RCPT TO: <address>"));
            return false;
        }

        session.addRecipient(recipient);
        session.sendReply(new Reply(ReplyCode.OK, "OK"));
        return true;
    }

    private String getRecipient() {
        if (line.toUpperCase().startsWith(RCPT_TO)) {
            String recipient = line.substring(RCPT_TO.length());
            if (!isBlank(recipient)) {
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
