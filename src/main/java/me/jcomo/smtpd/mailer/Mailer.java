package me.jcomo.smtpd.mailer;

public interface Mailer {
    void send(String from, String to, String data);
}
