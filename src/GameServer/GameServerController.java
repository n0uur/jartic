package GameServer;

import Shared.GamePacket.ClientPacket;

import java.util.ArrayList;

public class GameServerController {

    private ArrayList<ClientPacket> packets;

    public GameServerController() {
        this.packets = new ArrayList<ClientPacket>();
    }

    public void addNetworkIncomePacket(ClientPacket clientPacket) {
        this.packets.add(clientPacket);
    }

}
