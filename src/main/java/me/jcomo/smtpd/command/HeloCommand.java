package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Reply;
import me.jcomo.smtpd.ReplyCode;
import me.jcomo.smtpd.Session;

public class HeloCommand implements Command {
    private final Session session;
    private final String line;

    public HeloCommand(Session session, String line) {
        this.session = session;
        this.line = line;
    }

    @Override
    public boolean execute() {
        String[] args = getArgs();
        if (args.length < 2) {
            session.sendReply(new Reply(ReplyCode.SYNTAX_ERROR, "Syntax: HELO <domain>"));
            return false;
        }

        String domain = args[1];
        session.setDomain(domain);
        session.sendReply(new Reply(ReplyCode.OK, "HELO localhost"));
        return true;
    }

    @Override
    public String getName() {
        return "HELO";
    }

    private String[] getArgs() {
        return line.split("\\s+");
    }
}
