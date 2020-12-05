package GameServer;

import Shared.Model.GamePacket.ClientPacket;
import Shared.Logger.ServerLog;

import java.util.ArrayList;

public class GameServer {

    // server worker

    private NetworkListener networkListener;
    private Thread networkListenerThread;

    private GameServerPacketHandler gameServerPacketHandler;
    private Thread gameServerPacketHandlerThead;

    private ArrayList<ClientPacket> packets;

    // game object

    // private ArrayList<Player> players;

    //

    public GameServer() {
        ServerLog.Log("Initializing Server..");

        this.packets = new ArrayList<ClientPacket>();

        ServerLog.Log("Creating Server worker & listener..");

        this.networkListener = new NetworkListener(this);
        this.networkListenerThread = new Thread(this.networkListener);
        this.networkListenerThread.start();

        this.gameServerPacketHandler = new GameServerPacketHandler(this);

        ServerLog.Log("Creating game object..");

        ServerLog.Log("Server start successfully..");
    }

    public void addNetworkIncomePacket(ClientPacket clientPacket) {
        this.packets.add(clientPacket);
    }

    public ArrayList<ClientPacket> getPackets() {
        return this.packets;
    }

}
