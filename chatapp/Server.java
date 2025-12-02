package chatapp;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started on port 5000...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket);
                clientHandlers.add(handler);

                Thread t = new Thread(handler);
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast message to all clients
    public static void broadcast(String message, ClientHandler excludeUser) {
        for (ClientHandler aClient : clientHandlers) {
            if (aClient != excludeUser) {
                aClient.sendMessage(message);
            }
        }
    }

    // Remove client from the list when disconnected
    public static void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
    }
}

// Handles communication with a single client
class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String msg;
        try {
            while ((msg = br.readLine()) != null) {
                System.out.println("Received: " + msg);
                Server.broadcast(msg, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
              Server.removeClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        pw.println(message);
    }
}
