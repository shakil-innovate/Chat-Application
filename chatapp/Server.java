package chatapp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started. Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            Thread readThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = br.readLine()) != null) {
                        System.out.println("Client: " + msg);
                    }
                } catch (Exception e) {
                }
            });

            readThread.start();

            while (true) {
                String msg = scanner.nextLine();
                pw.println(msg);
            }

        }   catch(Exception e){
                e.printStackTrace();
            }
        }
    }
