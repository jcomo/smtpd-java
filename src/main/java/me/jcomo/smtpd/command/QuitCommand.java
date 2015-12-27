package me.jcomo.smtpd.command;

public class QuitCommand implements Command {
    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public String getName() {
        return "QUIT";
    }
}
