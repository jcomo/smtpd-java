package me.jcomo.smtpd.command;

import me.jcomo.smtpd.server.Reply;
import me.jcomo.smtpd.server.ReplyCode;
import me.jcomo.smtpd.server.Session;

public class DataCommand implements Command {
    private final Session session;

    public DataCommand(Session session) {
        this.session = session;
    }

    @Override
    public boolean execute() {
        session.sendReply(new Reply(ReplyCode.DATA_START,
                "start mail input; end with <CRLF>.<CRLF>"));

        session.getMessageBuffer().receiveData();
        session.getMessageBuffer().done();

        session.sendReply(new Reply(ReplyCode.OK, "OK"));
        return true;
    }

    @Override
    public String getName() {
        return "DATA";
    }
}
