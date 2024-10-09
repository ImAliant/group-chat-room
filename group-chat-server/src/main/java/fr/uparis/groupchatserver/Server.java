package fr.uparis.groupchatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        System.out.println("Server is running...");

        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clienthandler = new ClientHandler(socket);

                Thread thread = new Thread(clienthandler);
                thread.start();

                if (socket.isClosed()) {
                    System.out.println("A client has disconnected");
                }
            }
        } catch (IOException e) {
            close();
        }
    }

    public void close() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
