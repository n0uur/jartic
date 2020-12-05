package Shared.Model.GamePacket;

import GameServer.Model.ServerPlayer;
import GameServer.NetworkListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;

public abstract class ServerPacket extends Packet {

    public void sendToClient(int peerId) {

        ServerPlayer targetServerPlayer = ServerPlayer.getPlayer(peerId);

        Gson gson = new Gson();
        String sendString = gson.toJson(this);
        byte[] sendData = sendString.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, targetServerPlayer.getPlayerIp(), targetServerPlayer.getPlayerPort());

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
