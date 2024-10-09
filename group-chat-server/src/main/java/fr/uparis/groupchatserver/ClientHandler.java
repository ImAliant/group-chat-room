package fr.uparis.groupchatserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private String username;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = reader.readLine();
            clients.add(this);

            broadcast(String.format("SERVER: %s has entered the chat!", username));
        } catch (IOException e) {
            close(socket, reader, writer);
        }
    }

    private void broadcast(String messageToSend) {
        for (ClientHandler client: clients) {
            try {
                if (client != this) {
                    client.writer.write(messageToSend);
                    client.writer.newLine();
                    client.writer.flush();
                }
            } catch (IOException e) {
                close(socket, reader, writer);
            }
        }
    }

    private void close(Socket socket, BufferedReader reader, BufferedWriter writer) {
        removeClient();

        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeClient() {
        clients.remove(this);
        broadcast(String.format("SERVER: %s has left the chat!", username));
    }
 
    @Override
    public void run() {
        String message;

        while (socket.isConnected()) {
            try {
                message = reader.readLine();
                if (message == null) {
                    throw new IOException();
                }

                broadcast(String.format("%s: %s", username, message));
            } catch (IOException e) {
                close(socket, reader, writer);
                break;
            }
        }
    }
}
