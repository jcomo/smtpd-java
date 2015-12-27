package me.jcomo.smtpd;

import me.jcomo.smtpd.command.Command;
import me.jcomo.smtpd.command.CommandFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SMTPConnection implements Runnable {
    private Socket socket;

    public SMTPConnection(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            Session session = new Session(out, in);
            SMTPProtocol protocol = new SMTPProtocol();
            CommandFactory commands = new CommandFactory(session, in);

            session.sendResponse("220 localhost Simple Mail Transfer Service Ready");

            String line;
            while ((line = in.readLine()) != null) {
                Command command = commands.getCommand(line);
                if (command != null) {
                    try {
                        protocol.transition(command);
                    } catch (IllegalStateException e) {
                        session.sendResponse("503 Bad sequence of commands");
                    }
                } else {
                    session.sendResponse("500 Command unrecognized");
                }

                if (protocol.isTerminated()) {
                    session.sendResponse("221 localhost Service closing transmission channel");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
