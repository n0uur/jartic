package GameServer;

import GameServer.Model.Player;
import Shared.GamePacket.C2S_JoinGame;
import Shared.GamePacket.ClientPacket;
import Shared.GamePacket.Packet;
import Shared.GamePacket.S2C_AcceptJoinGameRequest;
import Shared.Model.GameConfig;
import Shared.Model.GameServer;

import java.util.ArrayList;

public class GameServerPacketHandler implements Runnable {

    private GameServerController gameServerController;

    private boolean isDestroy;

    public GameServerPacketHandler(GameServerController gameServerController) {
        this.gameServerController = gameServerController;
    }

    public void destroy() {
        this.isDestroy = true;
    }

    @Override
    public void run() {
        while(!isDestroy) {
            ArrayList<ClientPacket> workQueue = gameServerController.getPackets();
            if(workQueue.size() > 0) {
                ClientPacket currentWork = workQueue.remove(0);

                if(currentWork.PacketId == Packet.PacketID.C2S_JoinGame) {

                    C2S_JoinGame gamePacket = (C2S_JoinGame) currentWork;

                    Player newPlayer = new Player(gamePacket.playerName);

                    newPlayer.setPlayerResponseAddress(gamePacket.playerIp, gamePacket.playerPort);

                    S2C_AcceptJoinGameRequest responsePacket = new S2C_AcceptJoinGameRequest();

                    if(Player.getPlayers().size() >= GameConfig.MAX_PLAYER) {
                        responsePacket.isRoomFull = true;
                    }
                    else {
                        responsePacket.playerToken = newPlayer.getPlayerToken();
                        responsePacket.gameStatus = GameServer.gameStatus.GAME_PLAYING;
                        responsePacket.playerProfile = newPlayer.getPlayerProfile();
                    }
                }
                else if (currentWork.PacketId == Packet.PacketID.C2S_ChatMessage){
                    // todo..
                }
            }
        }
    }
}
