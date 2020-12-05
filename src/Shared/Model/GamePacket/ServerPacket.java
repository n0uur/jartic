package Shared.Model.GamePacket;

import GameServer.Model.ServerPlayer;
import GameServer.NetworkListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public abstract class ServerPacket extends Packet {

    public void sendToClient(int peerId) {

        ServerPlayer targetServerPlayer = ServerPlayer.getPlayer(peerId);

        sendToClient(targetServerPlayer.getPlayerIp(), targetServerPlayer.getPlayerPort());

    }

    public void sendToClient(InetAddress playerIP, int playerPort) {
        Gson gson = new Gson();
        String sendString = gson.toJson(this);
        byte[] sendData = sendString.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, playerIP, playerPort);

        try {
            NetworkListener.getDatagramSocket().send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastToClient() {

        ServerPlayer.getPlayers().forEach((player) -> {
            sendToClient(player.getPeerId());
        });

    }
}
