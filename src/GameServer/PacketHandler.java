package GameServer;

import GameServer.Model.ServerPlayer;
import Shared.Logger.ServerLog;
import Shared.Model.GamePacket.C2S_JoinGame;
import Shared.Model.GamePacket.ClientPacket;
import Shared.Model.GamePacket.Packet;
import Shared.Model.GamePacket.S2C_AcceptJoinGameRequest;
import Shared.Model.GameConfig;

import java.util.ArrayList;

public class PacketHandler implements Runnable {

    private GameServer gameServer;

    private boolean isDestroy;

    public PacketHandler(GameServer gameServer) {
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

                    S2C_AcceptJoinGameRequest responsePacket = new S2C_AcceptJoinGameRequest();

                    if(ServerPlayer.getPlayers().size() >= GameConfig.MAX_PLAYER) {
                        responsePacket.isRoomFull = true;
                    }
                    else {
                        ServerPlayer newServerPlayer = new ServerPlayer(gamePacket.playerName);

                        newServerPlayer.setPlayerResponseAddress(gamePacket.playerIp, gamePacket.playerPort);

                        responsePacket.playerToken = newServerPlayer.getPlayerToken();
                        responsePacket.gameStatus = Shared.Model.GameServer.gameStatus.GAME_PLAYING;
                        responsePacket.playerProfile = newServerPlayer.getPlayerProfile();
                    }

                    responsePacket.sendToClient(newServerPlayer.getPeerId());
                }
                else {
                    ServerPlayer packetServerPlayer = ServerPlayer.getPlayer(currentWork.playerToken);

                    if(packetServerPlayer != null) { // exists player
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
