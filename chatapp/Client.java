package chatapp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter Server IP: ");
            String serverIP = scanner.nextLine();

            Socket socket = new Socket(serverIP, 5000);
            System.out.println("Connected to Server!");

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            Thread readThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = br.readLine()) != null) {
                        System.out.println("Server: " + msg);
                    }
                } catch (Exception e) { }
            });

            readThread.start();

            while (true) {
                String msg = scanner.nextLine();
                pw.println(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
