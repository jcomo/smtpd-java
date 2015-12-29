package me.jcomo.smtpd.mailer;

import java.io.PrintWriter;

public class DebugFileMailer implements Mailer {
    private final PrintWriter out;

    public DebugFileMailer(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void send(String from, String to, String data) {
        out.println("------------ BEGIN MESSAGE ------------");
        out.println("FROM: " + from);
        out.println("TO: " + to);
        out.println();
        out.println(data);
        out.println("------------  END MESSAGE  -----------");
        out.flush();
    }
}
