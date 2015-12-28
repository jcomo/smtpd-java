package me.jcomo.smtpd.command;

import me.jcomo.smtpd.server.Reply;
import me.jcomo.smtpd.server.ReplyCode;
import me.jcomo.smtpd.server.Session;

import java.util.Optional;

import static me.jcomo.smtpd.util.EmailUtils.parseEmailAddress;
import static me.jcomo.smtpd.util.StringUtils.isBlank;

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
        String address = getRawAddress();
        if (null == address) {
            session.sendReply(new Reply(ReplyCode.SYNTAX_ERROR, "Syntax: RCPT TO: <address>"));
            return false;
        }

        Optional<String> recipient = parseEmailAddress(getRawAddress());
        if (!recipient.isPresent()) {
            session.sendReply(new Reply(ReplyCode.INVALID_MAILBOX_SYNTAX,
                    "invalid email address: " + address));
            return false;
        }

        session.addRecipient(recipient.get());
        session.sendReply(new Reply(ReplyCode.OK, "OK"));
        return true;
    }

    private String getRawAddress() {
        if (line.toUpperCase().startsWith(RCPT_TO)) {
            String recipient = line.substring(RCPT_TO.length());
            if (!isBlank(recipient)) {
                return recipient.trim();
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return "RCPT";
    }
}
