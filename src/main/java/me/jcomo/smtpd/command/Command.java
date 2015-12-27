package me.jcomo.smtpd.command;

public interface Command {
    boolean execute();
    String getName();
}
