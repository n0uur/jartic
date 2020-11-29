package Shared.GamePacket;

import GameClient.Network.NetworkSocket;
import GameServer.Model.Player;
import Shared.Model.GameConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class ClientPacket extends Packet {
    public String playerToken;
    public InetAddress playerIp;
    public int playerPort;

    public void sendToServer() {

        try {

            InetAddress IPAddress = null;
            IPAddress = InetAddress.getByName(GameConfig.getServerAddress());

            Gson gson = new Gson();
            String jsonData = gson.toJson(this);

            byte[] packetData = jsonData.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, IPAddress, GameConfig.SERVER_GAME_PORT);

            NetworkSocket.getClientSocket().send(sendPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
