package me.jcomo.smtpd.server;

import me.jcomo.smtpd.command.Command;
import me.jcomo.smtpd.command.CommandFactory;
import me.jcomo.smtpd.mailer.DebugFileMailer;
import me.jcomo.smtpd.message.SimpleMessageBuffer;
import me.jcomo.smtpd.protocol.SMTPProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import static me.jcomo.smtpd.util.LogUtils.formatStackTrace;

public class SMTPConnection implements Runnable {
    private static final Logger logger = Logger.getLogger(SMTPConnection.class.getName());

    private String hostname;
    private Socket socket;

    public SMTPConnection(String hostname, Socket socket) {
        this.hostname = hostname;
        this.socket = socket;
    }

    public void run() {
        try (
                final PrintWriter out = new PrintWriter(socket.getOutputStream());
                final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            final SMTPProtocol protocol = new SMTPProtocol();
            final DebugFileMailer mailer = new DebugFileMailer(new PrintWriter(System.out));
            final SimpleMessageBuffer messageBuffer = new SimpleMessageBuffer(in, mailer);
            final Session session = new Session(hostname, out, messageBuffer);
            final CommandFactory commands = new CommandFactory(session);

            session.sendReply(new Reply(ReplyCode.SERVICE_READY,
                    hostname + " Simple Mail Transfer Service Ready"));

            String line;
            while ((line = in.readLine()) != null) {
                try {
                    Command command = commands.getCommand(line);
                    protocol.transition(command);
                } catch (NoSuchElementException e) {
                    session.sendReply(new Reply(ReplyCode.UNKNOWN, "unrecognized command"));
                } catch (IllegalArgumentException e) {
                    session.sendReply(new Reply(ReplyCode.NOT_IMPLEMENTED, "not implemented"));
                } catch (IllegalStateException e) {
                    session.sendReply(new Reply(ReplyCode.BAD_SEQUENCE, "bad command sequence"));
                }

                if (protocol.isTerminated()) {
                    session.sendReply(new Reply(ReplyCode.SERVICE_CLOSING,
                            hostname + " Service closing transmission channel"));
                    break;
                }
            }
        } catch (Exception e) {
            logger.severe(formatStackTrace(e));
        } finally {
            gracefullyCloseSocket();
        }
    }

    private void gracefullyCloseSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                // ¯\_(ツ)_/¯
                logger.warning("Failed to close client socket " + socket.getInetAddress());
            }
        }
    }
}
