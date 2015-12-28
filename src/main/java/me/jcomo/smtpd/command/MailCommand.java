package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Reply;
import me.jcomo.smtpd.ReplyCode;
import me.jcomo.smtpd.Session;

import static me.jcomo.smtpd.command.StringUtils.isBlank;

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
            session.sendReply(new Reply(ReplyCode.SYNTAX_ERROR, "Syntax: MAIL FROM: <address>"));
            return false;
        }

        session.setSender(sender);
        session.sendReply(new Reply(ReplyCode.OK, "OK"));
        return true;
    }

    private String getSender() {
        if (line.toUpperCase().startsWith(MAIL_FROM)) {
            String sender = line.substring(MAIL_FROM.length());
            if (!isBlank(sender)) {
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
