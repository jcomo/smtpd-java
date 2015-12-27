package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Session;

public class HeloCommand implements Command {
    private Session session;
    private String line;

    public HeloCommand(Session session, String line) {
        this.session = session;
        this.line = line;
    }

    @Override
    public boolean execute() {
        // TODO: check for server arg
        session.sendResponse("250 HELO");
        return true;
    }

    @Override
    public String getName() {
        return "HELO";
    }
}
