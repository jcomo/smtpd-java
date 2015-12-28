package me.jcomo.smtpd.command;

import me.jcomo.smtpd.server.Session;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class CommandFactory {
    private static final int COMMAND_NAME_LENGTH = 4;
    private static final Set<String> unimplementedCommands = new HashSet<>();

    static {
        unimplementedCommands.add("EHLO");
        unimplementedCommands.add("EXPN");
        unimplementedCommands.add("VRFY");
        unimplementedCommands.add("SOML");
        unimplementedCommands.add("SAML");
    }

    private final Session session;

    public CommandFactory(Session session) {
        this.session = session;
    }

    public Command getCommand(String line) {
        String name = commandName(line);

        switch (name) {
            case "HELO":
                return new HeloCommand(session, line);
            case "MAIL":
                return new MailCommand(session, line);
            case "RCPT":
                return new RcptCommand(session, line);
            case "DATA":
                return new DataCommand(session);
            case "RSET":
                return new RsetCommand(session);
            case "NOOP":
                return new NoopCommand(session);
            case "QUIT":
                return new QuitCommand();
            default:
                throw errorForUnspecifiedCommand(name);
        }
    }

    private String commandName(String line) {
        String name = "";
        if (line.length() >= COMMAND_NAME_LENGTH) {
            name = line.substring(0, COMMAND_NAME_LENGTH);
        }

        return name.toUpperCase();
    }

    private RuntimeException errorForUnspecifiedCommand(String name) {
        if (unimplementedCommands.contains(name)) {
            return new NotImplementedException();
        } else {
            return new NoSuchElementException("No element found: " + name);
        }
    }
}
