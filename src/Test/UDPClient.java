package Test;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class UDPClient {
    public static void main(String args[]) {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        String sentence;
        Scanner sc = new Scanner(System.in);
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost"); // not implements AutoCloseabler
            DatagramPacket sendPacket;
            DatagramPacket receivePacket;
            sentence = sc.nextLine();

            sendData = sentence.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);

            while (true) {
                Arrays.fill(receiveData, (byte) 0);
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                if(modifiedSentence != null)
                    System.out.println("FROM SERVER:" + modifiedSentence);

            }
//            clientSocket.close();
        } catch (SocketException ex) {
            System.out.println(ex);
        } catch (UnknownHostException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
