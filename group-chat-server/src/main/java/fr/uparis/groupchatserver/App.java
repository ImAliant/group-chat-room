package fr.uparis.groupchatserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Hello world!
 */
public class App {
    private static final int DEFAULT_PORT = 5555;

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        ServerSocket serverSocket = new ServerSocket(port);
        Server server = new Server(serverSocket);

        server.start();
    }
}
