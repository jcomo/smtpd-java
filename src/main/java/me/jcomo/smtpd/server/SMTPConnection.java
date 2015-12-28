package me.jcomo.smtpd.server;

import me.jcomo.smtpd.command.Command;
import me.jcomo.smtpd.command.CommandFactory;
import me.jcomo.smtpd.message.SimpleMessageBuffer;
import me.jcomo.smtpd.protocol.SMTPProtocol;

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
                final PrintWriter out = new PrintWriter(socket.getOutputStream());
                final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            final SMTPProtocol protocol = new SMTPProtocol();
            final SimpleMessageBuffer messageBuffer = new SimpleMessageBuffer(in);
            final Session session = new Session(out, messageBuffer);
            final CommandFactory commands = new CommandFactory(session);

            session.sendReply(new Reply(ReplyCode.SERVICE_READY,
                    "localhost Simple Mail Transfer Service Ready"));

            String line;
            while ((line = in.readLine()) != null) {
                try {
                    Command command = commands.getCommand(line);
                    protocol.transition(command);
                } catch (IllegalArgumentException e) {
                    session.sendReply(new Reply(ReplyCode.UNKNOWN, "unrecognized command"));
                } catch (IllegalStateException e) {
                    session.sendReply(new Reply(ReplyCode.BAD_SEQUENCE, "bad command sequence"));
                }

                if (protocol.isTerminated()) {
                    session.sendReply(new Reply(ReplyCode.SERVICE_CLOSING,
                            "localhost Service closing transmission channel"));
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
