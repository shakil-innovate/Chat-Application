package chatapp;

import java.awt.desktop.SystemEventListener;
import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
    public static void main(String[] args){
        try{
            Scanner sc=new Scanner(System.in);

            System.out.println("Enter Server IP: ");
            String serverIP=sc.nextLine();

            Socket socket=new Socket(serverIP,5000);
            System.out.println("connceted to server!");

            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);

            Thread readThread=new Thread(()->{
                try{
                    String msg;
                    while((msg=br.readLine())!= null){
                        System.out.println(msg);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            });
           readThread.start();

        while(true){
            String msg=sc.nextLine();
            pw.println(msg);
        }
  }catch (IOException e){
            e.printStackTrace();
        }
    }
}