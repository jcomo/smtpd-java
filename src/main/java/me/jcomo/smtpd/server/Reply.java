package me.jcomo.smtpd.server;

public class Reply {
    private final ReplyCode code;
    private final String message;

    public Reply(ReplyCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public String render() {
        return code.getValue() + " " + message + "\r\n";
    }

    @Override
    public String toString() {
        return "Reply{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reply reply = (Reply) o;

        if (code != reply.code) return false;
        return message.equals(reply.message);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
