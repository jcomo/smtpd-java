package me.jcomo.smtpd.protocol;

public enum State {
    INIT,
    HELO,
    MAIL,
    RCPT,
    QUIT,
}
