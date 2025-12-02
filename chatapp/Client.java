package chatapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Chat App");
    private JTextArea messageArea = new JTextArea(20, 40);
    private JTextField textField = new JTextField(40);
    private JButton sendButton = new JButton("Send");

    public Client(String serverAddress) throws IOException {
        Socket socket = new Socket(serverAddress, 12345);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // GUI setup
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(textField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Send message when Enter pressed
        textField.addActionListener(e -> sendMessage());
        sendButton.addActionListener(e -> sendMessage());

        // Thread to read messages from server
        new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    messageArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() {
        String msg = textField.getText();
        if (!msg.isEmpty()) {
            out.println(msg);
            messageArea.append("Me: " + msg + "\n"); // show your own message
            textField.setText("");
        }
    }

    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog(
                "Enter server IP address:", "127.0.0.1");
        new Client(serverAddress);
    }
}