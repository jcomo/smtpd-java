package me.jcomo.smtpd;

public enum State {
    INIT,
    HELO,
    MAIL,
    RCPT,
    QUIT,
}
