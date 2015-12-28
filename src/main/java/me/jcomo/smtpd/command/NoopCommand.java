package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Reply;
import me.jcomo.smtpd.ReplyCode;
import me.jcomo.smtpd.Session;

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
