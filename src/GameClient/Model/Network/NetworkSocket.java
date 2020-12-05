package GameClient.Model.Network;

import java.net.DatagramSocket;
import java.net.SocketException;

public class NetworkSocket {
    private static DatagramSocket clientSocket;

    public static DatagramSocket getClientSocket() {
        if(clientSocket == null) {
            try {
                clientSocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return clientSocket;
    }
}
