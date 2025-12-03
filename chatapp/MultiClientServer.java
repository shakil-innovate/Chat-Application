package chatapp;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class MultiClientServer{
        private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started on port 5000...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket);
                clientHandlers.add(handler);

                      Thread t=new Thread(()->{
                        String msg;
                        try{
                            BufferedReader br=handler.getReader();
                            while ((msg = br.readLine()) != null){
                                System.out.println("Received: "+msg);
                                broadcast(msg,handler);
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                        }finally {
                            try{
                                handler.getSocket().close();
                                removeClient(handler);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                      });
                      t.start();
                  }
                }catch(IOException e){
                    e.printStackTrace();
                }
        }

        public static void broadcast(String msg, ClientHandler excludeUser){
                for(ClientHandler aClient:clientHandlers){
                    if(aClient!= excludeUser){
                        aClient.sendMessage(msg);
                    }
                }
        }

        public static void removeClient(ClientHandler client){
            clientHandlers.remove(client);
        }
}