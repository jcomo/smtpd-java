package me.jcomo.smtpd.command;

import me.jcomo.smtpd.server.Reply;
import me.jcomo.smtpd.server.ReplyCode;
import me.jcomo.smtpd.server.Session;

public class NoopCommand implements Command {
    private final Session session;

    public NoopCommand(Session session) {
        this.session = session;
    }

    @Override
    public boolean execute() {
        session.sendReply(new Reply(ReplyCode.OK, "noop"));
        return true;
    }

    @Override
    public String getName() {
        return "NOOP";
    }
}
