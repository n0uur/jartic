package GameServer;

import GameServer.Model.ServerPlayer;
import GameServer.Network.ServerNetworkListener;
import GameClient.Model.ClientPacket;
import Shared.Logger.ServerLog;
import Shared.Model.GamePacket.S2C_ChatMessage;
import Shared.Model.GamePacket.S2C_RequestHeartBeat;
import Shared.Model.GameServerStatus;

import java.util.ArrayList;

public class GameServer {

    // server worker

    private ServerNetworkListener serverNetworkListener;
    private Thread networkListenerThread;

    private ServerPacketHandler serverPacketHandler;
    private Thread packetHandlerThead;

    private ArrayList<ClientPacket> packets;

    // game object

    // private ArrayList<Player> players;
    private GameServerStatus currentGameStatus;

    private long lastBroadcastDataUpdate;

    //

    public GameServer() {
        ServerLog.Log("Initializing Server..");

        this.packets = new ArrayList<ClientPacket>();

        ServerLog.Log("Creating Server worker & listener..");

        this.serverNetworkListener = new ServerNetworkListener(this);
        this.networkListenerThread = new Thread(this.serverNetworkListener);
        this.networkListenerThread.start();

        this.serverPacketHandler = new ServerPacketHandler(this);
        this.packetHandlerThead = new Thread(this.serverPacketHandler);
        this.packetHandlerThead.start();

        ServerLog.Log("Creating game object..");

        ServerLog.Log("Server start successfully..");
    }

    public void update() {
        // todo : update game logic via time

        long currentTime = System.currentTimeMillis();

        // check if player not enough then reset game to waiting status !
        if(ServerPlayer.getPlayers().size() < 2) {

            if(this.getCurrentGameStatus() != GameServerStatus.GAME_WAITING) {
                this.setCurrentGameStatus(GameServerStatus.GAME_ENDED); // instant end game if player not enough.
            }

        }

        // check player connection is still alive
        synchronized (this) {
            for(int i = 0; i < ServerPlayer.getPlayers().size(); i++) {

                ServerPlayer player = ServerPlayer.getPlayers().get(i);

                if(currentTime - player.getLastResponse() > 5000 && !player.isRequestedHeartBeat()) {

                    ServerLog.Log(player.getPlayerProfile().getName() + " has no responses in 5 seconds..");

                    S2C_RequestHeartBeat requestPacket = new S2C_RequestHeartBeat();
                    requestPacket.sendToClient(player.getPeerId());

                    player.isRequestedHeartBeat(true);
                }

                if(currentTime - player.getLastResponse() > 10000 && player.isRequestedHeartBeat()) {

                    player.remove();

                    S2C_ChatMessage leftMessage = new S2C_ChatMessage();
                    leftMessage.flag = S2C_ChatMessage.messageFlag.MESSAGE_DANGER;
                    leftMessage.message = player.getPlayerProfile().getName() + " has lost his/her connection with our game :(";

                    ServerLog.Log(player.getPlayerProfile().getName() + " has lost his/her connection.");

                    leftMessage.broadcastToClient();

                    this.broadcastGameDataUpdate();

                }
            }
        }

        // broadcast game data update if it's too long
        if(this.lastBroadcastDataUpdate > 2000) {
            this.broadcastGameDataUpdate();
        }

        // todo :
        //  game logic up to game state..

        if(this.getCurrentGameStatus() == GameServerStatus.GAME_WAITING) {

        }

    }

    public void destroy() {
        ServerLog.Log("Server is shutting down..");

        this.serverNetworkListener.destroy();
        this.serverPacketHandler.destroy();

        // todo : tell all player server is closing..

        ServerLog.Log("Bye!");

    }

    public synchronized void addNetworkIncomePacket(ClientPacket clientPacket) {
        ServerLog.Log("Incoming packet ["+ clientPacket.PacketId +"]");
        this.packets.add(clientPacket);
    }

    public synchronized ClientPacket getQueuePacket() {
        return this.packets.remove(0);
    }

    public ArrayList<ClientPacket> getPackets() {
        return this.packets;
    }

    public GameServerStatus getCurrentGameStatus() {
        return currentGameStatus;
    }

    public void setCurrentGameStatus(GameServerStatus currentGameStatus) {
        this.currentGameStatus = currentGameStatus;
        this.broadcastGameDataUpdate();
    }

    public void broadcastGameDataUpdate() {
        // todo : broadcast player current game state, update game, game data
        this.lastBroadcastDataUpdate = System.currentTimeMillis();
    }
}
