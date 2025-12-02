package chatapp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter server IP: ");
            String serverIP = scanner.nextLine();

            Socket socket = new Socket(serverIP, 5000);
            System.out.println("Connected to server!");

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            // Thread to read messages from server
            Thread readThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = br.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readThread.start();

            // Main thread to send messages
            while (true) {
                String msg = scanner.nextLine();
                pw.println(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
