package me.jcomo.smtpd;

public interface StateMachine<T> {
    void transition(T command);
    boolean isTerminated();
}
