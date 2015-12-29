package me.jcomo.smtpd;

import me.jcomo.smtpd.server.SMTPConnection;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) throws Exception {
        final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(0, 1024, 60, TimeUnit.SECONDS, new SynchronousQueue<>());
        final ServerSocket server = new ServerSocket(8025);
        registerShutdownHook(threadPool);

        while (true) {
            final Socket socket = server.accept();
            threadPool.submit(new SMTPConnection((socket)));
        }
    }

    private static void registerShutdownHook(ExecutorService service) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutting down server.");
                service.shutdown();

                try {
                    service.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted while shutting down.");
                }
            }
        });
    }
}
