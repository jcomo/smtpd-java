package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Session;

import java.io.*;

public class CommandFactory {
    private Session session;
    private BufferedReader inputStream;

    public CommandFactory(Session session, BufferedReader inputStream) {
        this.session = session;
        this.inputStream = inputStream;
    }

    public Command getCommand(String line) {
        // TODO: error checking
        String commandName = line.split(" ")[0];

        if (commandName.startsWith("HELO")) {
            return new HeloCommand(session, line);
        } else if (commandName.startsWith("MAIL")) {
            return new MailCommand(session, line);
        } else if (commandName.startsWith("RCPT")) {
            return new RcptCommand(session, line);
        } else if (commandName.startsWith("QUIT")) {
            return new QuitCommand();
        }

        return null;
    }
}
