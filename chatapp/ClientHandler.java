package chatapp;

import  java.net.*;
import java.io.*;

public class ClientHandler{
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public ClientHandler(Socket socket){
        try{
        this.socket=socket;
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw=new PrintWriter(socket.getOutputStream(),true);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public BufferedReader getReader(){
        return br;
    }
    public  Socket getSocket(){
        return socket;
    }

    public void sendMessage(String msg){
        pw.println(msg);
    }
}