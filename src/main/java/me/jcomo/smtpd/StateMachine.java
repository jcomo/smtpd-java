package me.jcomo.smtpd;

import me.jcomo.smtpd.command.Command;

public interface StateMachine {
    void transition(Command command);
    boolean isTerminated();
}
