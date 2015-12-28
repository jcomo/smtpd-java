package me.jcomo.smtpd;

import me.jcomo.smtpd.server.SMTPConnection;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8025);
        while (true) {
            Socket socket = server.accept();

            Thread thread = new Thread(new SMTPConnection(socket));
            thread.start();
        }
    }
}
