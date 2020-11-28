package GameServer;

import Shared.GamePacket.ClientPacket;
import Shared.GamePacket.Packet;
import Shared.Logger.ServerLog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class GameServerController {

    private NetworkListener networkListener;
    private Thread networkListenerThread;

    private GameServerPacketHandler gameServerPacketHandler;
    private Thread gameServerPacketHandlerThead;

    private ArrayList<ClientPacket> packets;

    public GameServerController() {
        ServerLog.Log("Initializing Server..");

        this.packets = new ArrayList<ClientPacket>();

        ServerLog.Log("Creating Socket listener..");

        this.networkListener = new NetworkListener(this);
        this.networkListenerThread = new Thread(this.networkListener);
        this.networkListenerThread.start();

        this.gameServerPacketHandler = new GameServerPacketHandler(this);
    }

    public void addNetworkIncomePacket(ClientPacket clientPacket) {
        this.packets.add(clientPacket);
    }

    public ArrayList<ClientPacket> getPackets() {
        return this.packets;
    }

}
