package Test;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer {
    public static void main(String args[]) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(9876);
            DatagramPacket receivePacket;
            DatagramPacket sendPacket;
            InetAddress IPAddress;
            String sentence;
            int port;
            byte[] receiveData = new byte[1024];
            byte[] sendData;
            while (true) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                sentence = new String(receivePacket.getData());
                IPAddress = receivePacket.getAddress();
                port = receivePacket.getPort();
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();

                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                for(int i = 0; i < 10; i++) {
                    serverSocket.send(sendPacket);
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(IPAddress + " : " + port + " >> " + capitalizedSentence);
                }
                Scanner sc = new Scanner(System.in);
                for(int i = 0; i < 10; i++) {
                    byte[] sendback = sc.nextLine().getBytes();
                    sendPacket = new DatagramPacket(sendback, sendback.length, IPAddress, port);
                    System.out.println(IPAddress + " : " + port + " >> " + sendback);
                    serverSocket.send(sendPacket);
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
