package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Reply;
import me.jcomo.smtpd.ReplyCode;
import me.jcomo.smtpd.Session;

import java.io.IOException;

public class DataCommand implements Command {
    private final Session session;
    private final String line;

    public DataCommand(Session session, String line) {
        this.session = session;
        this.line = line;
    }

    @Override
    public boolean execute() {
        session.sendReply(new Reply(ReplyCode.DATA_START,
                "start mail input; end with <CRLF>.<CRLF>"));

        String line;
        try {
            while ((line = session.getInput().readLine()) != null) {
                System.out.println(line);
                if (".".equals(line)) {
                    System.out.println("done accepting data");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        session.sendReply(new Reply(ReplyCode.OK, "OK"));
        return true;
    }

    @Override
    public String getName() {
        return "DATA";
    }
}
