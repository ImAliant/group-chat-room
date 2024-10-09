package fr.uparis.groupchatclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    
    private String username;
    private Scanner scanner;

    public Client(Socket socket, String username, Scanner scanner) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            this.scanner = scanner;

            sendUsername();
        } catch (IOException e) {
            close(socket, reader, writer);
        }
    }

    public void sendMessage() {
        try {
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                writer.write(messageToSend);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            close(socket, reader, writer);
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            String messageReceived;

            while (socket.isConnected()) {
                try {
                    messageReceived = reader.readLine();
                    if (messageReceived == null) {
                        close(socket, reader, writer);
                        break;
                    }

                    System.out.println(messageReceived);
                } catch (IOException e) {
                    close(socket, reader, writer);
                }
            }
        }).start();
    }

    private void sendUsername() {
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            close(socket, reader, writer);
        }
    }

    private void close(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
