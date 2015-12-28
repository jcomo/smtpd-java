package me.jcomo.smtpd;

public class Reply {
    private final ReplyCode code;
    private final String message;

    public Reply(ReplyCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public String render() {
        return code.getValue() + " " + message;
    }
}
