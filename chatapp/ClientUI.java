package chatapp;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientUI extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter pw;

    public ClientUI() {
        setTitle("Chat App");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            String msg = inputField.getText();
            pw.println(msg);
            inputField.setText("");
        });

        connectToServer();

        setVisible(true);
    }

    private void connectToServer() {
        try {
            String ip = JOptionPane.showInputDialog(this, "Enter Server IP:");

            Socket socket = new Socket(ip, 5000);
            chatArea.append("Connected to server...\n");

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);

            Thread readThread = new Thread(() -> {
                String msg;
                try {
                    while ((msg = br.readLine()) != null) {
                        chatArea.append(msg + "\n");
                    }
                } catch (IOException e) {
                    chatArea.append("Disconnected.\n");
                }
            });

            readThread.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientUI::new);
    }
}
