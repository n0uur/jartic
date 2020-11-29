package Test;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient1 implements Runnable {

    String name;

    TCPClient1(String n) {
        name = n;
    }

    @Override
    public void run() {
        while(true) {
            Scanner sc = new Scanner(System.in);
            String sentence;
            String modifiedSentence;
            try (Socket clientSocket = new Socket("localhost", 6789);
                 DataOutputStream outToServer = new DataOutputStream( clientSocket.getOutputStream() );
                 BufferedReader inFromServer = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()));){
                sentence = sc.nextLine();
                while(!sentence.equals("end")) {
                    System.out.println("Send");
                    outToServer.writeBytes(sentence+"\n");
                    System.out.println("Wait for recieve!");
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("["+name+"] FROM SERVER: " + modifiedSentence);
                    sentence = sc.nextLine();
                }
            } catch (IOException ex) { System.out.println(ex); }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}