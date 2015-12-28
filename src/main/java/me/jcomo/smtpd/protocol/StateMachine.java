package me.jcomo.smtpd.protocol;

public interface StateMachine<T> {
    void transition(T command);
    boolean isTerminated();
}
