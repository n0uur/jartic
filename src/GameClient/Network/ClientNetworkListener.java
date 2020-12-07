package GameClient.Network;

import GameClient.GameClient;
import GameClient.Model.Network.NetworkSocket;
import GameServer.Model.ServerPacket;
import Shared.Logger.GameLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class ClientNetworkListener implements Runnable {

    private boolean isDestroy;
    private GameClient gameClient;
    private final Gson gson = new GsonBuilder().setLenient().create();

    byte[] receiveData = new byte[5000];

    public ClientNetworkListener(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void run() {

        DatagramSocket clientSocket = NetworkSocket.getClientSocket();
        DatagramPacket receivePacket;

        while (!isDestroy) {
            try {

                Arrays.fill(receiveData, (byte) 0); // clear input stream

                receivePacket = new DatagramPacket(receiveData, receiveData.length);

                clientSocket.receive(receivePacket);

                String jsonString = new String(receivePacket.getData()).trim(); // N0UR FIXED : BugID 193890182 : packet having invalid space after } !

//                GameLog.Log(jsonString);

                String packetName = JsonParser.parseString(jsonString).getAsJsonObject().get("PacketId").getAsString();

                Class<?> packetClass = Class.forName("Shared.Model.GamePacket." + packetName);

                ServerPacket realPacket = gson.fromJson(jsonString, (Type) packetClass);

                this.gameClient.addServerPacket(realPacket);

//                GameLog.log("Incoming packet from server : " + packetName);

                this.gameClient.serverResponse();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
