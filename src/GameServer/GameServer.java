package GameServer;

import GameServer.Model.ServerPlayer;
import Shared.Model.GamePacket.ClientPacket;
import Shared.Logger.ServerLog;
import Shared.Model.GamePacket.S2C_ChatMessage;
import Shared.Model.GamePacket.S2C_RequestHeartBeat;
import Shared.Model.GameServerStatus;
import com.sun.security.ntlm.Server;

import java.util.ArrayList;

public class GameServer {

    // server worker

    private NetworkListener networkListener;
    private Thread networkListenerThread;

    private PacketHandler packetHandler;
    private Thread packetHandlerThead;

    private ArrayList<ClientPacket> packets;

    // game object

    // private ArrayList<Player> players;
    private GameServerStatus.gameStatus currentGameStatus;

    private long lastBroadcastDataUpdate;

    //

    public GameServer() {
        ServerLog.Log("Initializing Server..");

        this.packets = new ArrayList<ClientPacket>();

        ServerLog.Log("Creating Server worker & listener..");

        this.networkListener = new NetworkListener(this);
        this.networkListenerThread = new Thread(this.networkListener);
        this.networkListenerThread.start();

        this.packetHandler = new PacketHandler(this);
        this.packetHandlerThead = new Thread(this.packetHandler);
        this.packetHandlerThead.start();

        ServerLog.Log("Creating game object..");

        ServerLog.Log("Server start successfully..");
    }

    public void update() {
        // todo : update game logic via time

        // check if player not enough then reset game to waiting status !
        if(ServerPlayer.getPlayers().size() < 2) {

            if(this.getCurrentGameStatus() != GameServerStatus.gameStatus.GAME_WAITING) {
                this.setCurrentGameStatus(GameServerStatus.gameStatus.GAME_ENDED); // instant end game if player not enough.
            }

        }

        // check player connection is still alive
        ServerPlayer.getPlayers().forEach((player) -> {

            if(player.getLastResponse() > 5000 && !player.isRequestedHeartBeat()) {

                S2C_RequestHeartBeat requestPacket = new S2C_RequestHeartBeat();
                requestPacket.sendToClient(player.getPeerId());

                player.isRequestedHeartBeat(true);
            }

            // todo :
            //  [!] check if player last reponse > 10 seconds & already request for heartbeat
            //  [!] then remove that player cause of he/her has lost connection...
            //  [!] and send broadcast message that he/her has left..
            if(player.getLastResponse() > 10000 && player.isRequestedHeartBeat()) {

                player.remove();

                S2C_ChatMessage leftMessage = new S2C_ChatMessage();
                leftMessage.flag = S2C_ChatMessage.messageFlag.MESSAGE_DANGER;
                leftMessage.message = player.getPlayerProfile().getName() + " has left.";

                leftMessage.broadcastToClient();

                this.broadcastGameDataUpdate();

            }
        });

        // broadcast game data update if it's too long
        if(this.lastBroadcastDataUpdate > 2000) {
            this.broadcastGameDataUpdate();
        }

        // todo :
        //  game logic up to game state..

    }

    public void destroy() {
        ServerLog.Log("Server is shutting down..");

        this.networkListener.destroy();
        this.packetHandler.destroy();

        // todo : tell all player server is closing..

        ServerLog.Log("Bye!");

    }

    public synchronized void addNetworkIncomePacket(ClientPacket clientPacket) {
        this.packets.add(clientPacket);
    }

    public ArrayList<ClientPacket> getPackets() {
        return this.packets;
    }

    public GameServerStatus.gameStatus getCurrentGameStatus() {
        return currentGameStatus;
    }

    public void setCurrentGameStatus(GameServerStatus.gameStatus currentGameStatus) {
        this.currentGameStatus = currentGameStatus;
        this.broadcastGameDataUpdate();
    }

    public void broadcastGameDataUpdate() {
        // todo : broadcast player current game state, update game, game data
        this.lastBroadcastDataUpdate = System.currentTimeMillis();
    }
}
