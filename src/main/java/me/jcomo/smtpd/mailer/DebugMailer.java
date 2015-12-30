package me.jcomo.smtpd.mailer;

import java.io.PrintWriter;

public class DebugMailer implements Mailer {
    private final PrintWriter out;

    public DebugMailer(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void send(String from, String to, String data) {
        synchronized (out) {
            out.println("------------ BEGIN MESSAGE ------------");
            out.println("FROM: " + from);
            out.println("TO: " + to);
            out.println();
            out.println(data);
            out.println("------------  END MESSAGE  ------------");
            out.println();
            out.flush();
        }
    }
}
