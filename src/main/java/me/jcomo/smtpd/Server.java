package me.jcomo.smtpd;

import me.jcomo.config.Configuration;
import me.jcomo.smtpd.server.SMTPConnection;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws Exception {
        final Configuration.Builder configBuilder = new Configuration.Builder()
                .withSystemProperties("smtpd");

        if (args.length > 0) {
            String arg = args[0];
            if ("-h".equals(arg)) {
                System.out.println("usage: smtpd [config]");
                return;
            }

            configBuilder.addValues(new File(arg));
        }

        final Configuration config = configBuilder.build();

        final int port = config.getInt("server.port", 8025);
        final String hostname = config.get("server.hostname", InetAddress.getLocalHost().getHostName());

        final int coreConnections = config.getInt("pool.connections.core", 0);
        final int maxConnections = config.getInt("pool.connections.max", 1024);
        final int idleTimeout = config.getInt("pool.idle.timeout", 60);

        final int shutdownTimeout = config.getInt("server.shutdown.timeout", 5);

        final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                coreConnections, maxConnections, idleTimeout, TimeUnit.SECONDS, new SynchronousQueue<>());
        final ServerSocket server = new ServerSocket(port);

        registerShutdownHook(threadPool, shutdownTimeout);
        logger.info("SMTP server " + hostname + " listening on port " + port);

        while (true) {
            final Socket socket = server.accept();
            threadPool.submit(new SMTPConnection(hostname, socket));
        }
    }

    private static void registerShutdownHook(final ExecutorService service, final int timeout) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Shutting down server");
                service.shutdown();

                try {
                    service.awaitTermination(timeout, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.info("Interrupted while shutting down");
                }
            }
        });
    }
}
