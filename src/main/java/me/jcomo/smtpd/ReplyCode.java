package me.jcomo.smtpd;

public enum ReplyCode {
    SERVICE_READY(220),
    SERVICE_CLOSING(221),
    OK(250),
    UNKNOWN(500),
    SYNTAX_ERROR(501),
    BAD_SEQUENCE(503);

    private final int value;

    ReplyCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
