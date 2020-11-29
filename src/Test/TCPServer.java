package Test;

import java.io.*;
import java.net.*;
public class TCPServer implements Runnable {


    @Override
    public void run() {
        while(true) {
            String clientSentence, capitalizedSentence;
            try(ServerSocket welcomeSocket = new ServerSocket(6789);
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream()); ){
                while(true) {
                    System.out.println("Waiting");
                    clientSentence = inFromClient.readLine();
                    System.out.println("Gotcha!");
                    if(clientSentence == null)
                        break;
                    capitalizedSentence = clientSentence.toUpperCase() + '\n';
                    outToClient.writeBytes(capitalizedSentence);
                }
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
}