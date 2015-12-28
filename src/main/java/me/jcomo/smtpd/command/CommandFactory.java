package me.jcomo.smtpd.command;

import me.jcomo.smtpd.Session;

import java.io.*;

public class CommandFactory {
    private static final int COMMAND_NAME_LENGTH = 4;

    private final Session session;
    private final BufferedReader inputStream;

    public CommandFactory(Session session, BufferedReader inputStream) {
        this.session = session;
        this.inputStream = inputStream;
    }

    public Command getCommand(String line) {
        switch (commandName(line)) {
            case "HELO":
                return new HeloCommand(session, line);
            case "MAIL":
                return new MailCommand(session, line);
            case "RCPT":
                return new RcptCommand(session, line);
            case "QUIT":
                return new QuitCommand();
            default:
                throw new IllegalArgumentException("No command found for: " + line);
        }
    }

    private String commandName(String line) {
        String name = "";
        if (line.length() >= COMMAND_NAME_LENGTH) {
            name = line.substring(0, COMMAND_NAME_LENGTH);
        }

        return name.toUpperCase();
    }
}
