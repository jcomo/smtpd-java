package me.jcomo.smtpd.command;

import me.jcomo.smtpd.server.Reply;
import me.jcomo.smtpd.server.ReplyCode;
import me.jcomo.smtpd.server.Session;

import java.util.Optional;

import static me.jcomo.smtpd.util.EmailUtils.parseEmailAddress;
import static me.jcomo.smtpd.util.StringUtils.isBlank;

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
        String address = getRawAddress();
        if (null == address) {
            session.sendReply(new Reply(ReplyCode.SYNTAX_ERROR, "Syntax: MAIL FROM: <address>"));
            return false;
        }

        Optional<String> sender = parseEmailAddress(address);
        if (!sender.isPresent()) {
            session.sendReply(new Reply(ReplyCode.INVALID_MAILBOX_SYNTAX,
                    "invalid email address: " + address));
            return false;
        }

        session.setSender(sender.get());
        session.sendReply(new Reply(ReplyCode.OK, "OK"));
        return true;
    }

    private String getRawAddress() {
        if (line.toUpperCase().startsWith(MAIL_FROM)) {
            String sender = line.substring(MAIL_FROM.length());
            if (!isBlank(sender)) {
                return sender.trim();
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "MAIL";
    }
}
