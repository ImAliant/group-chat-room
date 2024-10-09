package fr.uparis.groupchatclient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5555;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        try {
            String username = getUsername(scanner);

            tryClientStart(host, port, username, scanner);
        } catch (ConnectException e) {
            System.out.println("Server is not running");
        }
    }

    private static String getUsername(Scanner scanner) {
        System.out.println("Enter your username: ");
        return scanner.nextLine();
    }

    private static void tryClientStart(String host, int port, String username, Scanner scanner) throws IOException {
        Socket socket = new Socket(host, port);
        Client client = new Client(socket, username, scanner);
        client.listenForMessage();
        client.sendMessage();
    }
}
