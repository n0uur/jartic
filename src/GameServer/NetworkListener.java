package GameServer;

import Shared.Model.GamePacket.C2S_JoinGame;
import Shared.Model.GamePacket.ClientPacket;
import Shared.Logger.ServerLog;
import Shared.Model.GameConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.util.Arrays;

public class NetworkListener implements Runnable {

    private static DatagramSocket serverSocket;

    public static DatagramSocket getDatagramSocket() {
        if(NetworkListener.serverSocket == null) {
            try {
                NetworkListener.serverSocket = new DatagramSocket(GameConfig.SERVER_GAME_PORT);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return NetworkListener.serverSocket;
    }

    private GameServer gameServer;

    private final Gson gson = new GsonBuilder().setLenient().create();

    private boolean isDestroyed;

    public NetworkListener(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void destroy() {
        this.isDestroyed = true;
    }

    @Override
    public void run() {
        try {

            DatagramPacket receivePacket;

            DatagramPacket sendPacket;

            byte[] receiveData = new byte[5120];

            while(!isDestroyed) {

                Arrays.fill(receiveData, (byte) 0); // clear input stream

                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                getDatagramSocket().receive(receivePacket);

                String jsonString = new String(receivePacket.getData()).trim(); // N0UR FIXED : BugID 193890182 : packet having invalid space after } !

                System.out.println(jsonString);

                String packetName = JsonParser.parseString(jsonString).getAsJsonObject().get("PacketId").getAsString();

                Class<?> packetClass = Class.forName("Shared.Model.GamePacket." + packetName);

                ClientPacket realPacket = gson.fromJson(jsonString, (Type) packetClass);

                realPacket.playerIp = receivePacket.getAddress();
                realPacket.playerPort = receivePacket.getPort();

                this.gameServer.addNetworkIncomePacket(realPacket);

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
