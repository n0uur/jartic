package GameServer;

import Shared.Model.GamePacket.ClientPacket;
import Shared.Logger.ServerLog;
import com.sun.security.ntlm.Server;

import java.util.ArrayList;

public class GameServer {

    // server worker

    private NetworkListener networkListener;
    private Thread networkListenerThread;

    private PacketHandler packetHandler;
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

        this.packetHandler = new PacketHandler(this);

        ServerLog.Log("Creating game object..");

        ServerLog.Log("Server start successfully..");
    }

    public void update() {

    }

    public void destroy() {
        ServerLog.Log("Server is shutting down..");

        this.networkListener.destroy();
        this.packetHandler.destroy();

        ServerLog.Log("Bye!");

    }

    public void addNetworkIncomePacket(ClientPacket clientPacket) {
        this.packets.add(clientPacket);
    }

    public ArrayList<ClientPacket> getPackets() {
        return this.packets;
    }

}
