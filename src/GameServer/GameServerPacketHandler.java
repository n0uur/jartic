package GameServer;

import GameServer.Model.Player;
import Shared.Logger.GameLog;
import Shared.Logger.ServerLog;
import Shared.Model.GamePacket.C2S_JoinGame;
import Shared.Model.GamePacket.ClientPacket;
import Shared.Model.GamePacket.Packet;
import Shared.Model.GamePacket.S2C_AcceptJoinGameRequest;
import Shared.Model.GameConfig;

import java.util.ArrayList;

public class GameServerPacketHandler implements Runnable {

    private GameServer gameServer;

    private boolean isDestroy;

    public GameServerPacketHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void destroy() {
        this.isDestroy = true;
    }

    @Override
    public void run() {
        while(!isDestroy) {
            ArrayList<ClientPacket> workQueue = gameServer.getPackets();
            if(workQueue.size() > 0) {
                ClientPacket currentWork = workQueue.remove(0);

                if(currentWork.PacketId == Packet.PacketID.C2S_JoinGame) { // no player token packet is just only Join Game Packet!

                    C2S_JoinGame gamePacket = (C2S_JoinGame) currentWork;

                    Player newPlayer = new Player(gamePacket.playerName);

                    newPlayer.setPlayerResponseAddress(gamePacket.playerIp, gamePacket.playerPort);

                    S2C_AcceptJoinGameRequest responsePacket = new S2C_AcceptJoinGameRequest();

                    if(Player.getPlayers().size() >= GameConfig.MAX_PLAYER) {
                        responsePacket.isRoomFull = true;
                    }
                    else {
                        responsePacket.playerToken = newPlayer.getPlayerToken();
                        responsePacket.gameStatus = Shared.Model.GameServer.gameStatus.GAME_PLAYING;
                        responsePacket.playerProfile = newPlayer.getPlayerProfile();
                    }

                    responsePacket.sendToClient(newPlayer.getPeerId());
                }
                else {
                    Player packetPlayer = Player.getPlayer(currentWork.playerToken);

                    if(packetPlayer != null) { // exists player
                        if(currentWork.PacketId == Packet.PacketID.C2S_ChatMessage) {

                        }
                        else if(currentWork.PacketId == Packet.PacketID.C2S_HeartBeat) {

                        }
                        else if(currentWork.PacketId == Packet.PacketID.C2S_RequestUpdatePlayers) { // update all players

                        }
                        else if(currentWork.PacketId == Packet.PacketID.C2S_RequestUpdateProfile) { // update only his/her profile

                        }
                        else if(currentWork.PacketId == Packet.PacketID.C2S_RequestUpdateWhiteBoard) {

                        }
                        else if(currentWork.PacketId == Packet.PacketID.C2S_SelectWord) { // drawing player selected word!

                        }
                        else if(currentWork.PacketId == Packet.PacketID.C2S_UpdateWhiteBoard) { // drawing player update his/her white board

                        }


                        // more packet to add above
                        else {
                            ServerLog.Error("Invalid packet from client!");
                        }
                    }
                    else { // no this is not real player
                        ServerLog.Error("Incorrect player income packet!");
                    }

                }
            }
        }
    }
}
