package GameServer;

import GameClient.Model.ClientPacket;
import GameServer.Model.ServerPlayer;
import Shared.Logger.ServerLog;
import Shared.Model.GamePacket.*;
import Shared.Model.GameConfig;
import Shared.Model.GameServerStatus;

public class ServerPacketHandler implements Runnable {

    private GameServer gameServer;

    private boolean isDestroy;

    public ServerPacketHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void destroy() {
        this.isDestroy = true;
    }

    @Override
    public void run() {
        while(!isDestroy) {
            if(gameServer.getPackets().size() > 0) {

                ClientPacket currentWork = gameServer.getQueuePacket();

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
                        responsePacket.gameStatus = Shared.Model.GameServerStatus.gameStatus.GAME_PLAYING;
                        responsePacket.playerProfile = newServerPlayer.getPlayerProfile();

                        // todo : broadcast chat message that player is join!, update game data
                    }

                    responsePacket.sendToClient(gamePacket.playerIp, gamePacket.playerPort);
                }
                else {
                    ServerPlayer packetServerPlayer = ServerPlayer.getPlayer(currentWork.playerToken);

                    if (packetServerPlayer != null) { // exists player

                        packetServerPlayer.isRequestedHeartBeat(false);
                        packetServerPlayer.response();

                        if (currentWork.PacketId == Packet.PacketID.C2S_ChatMessage) {
                            C2S_ChatMessage gamePacket = (C2S_ChatMessage) currentWork;

                            if (this.gameServer.getCurrentGameStatus() == GameServerStatus.gameStatus.GAME_PLAYING) {
                                // todo : if player is drawer == yes ; ignore.

                                // todo : if player is answer corrected answer; broadcast that message ;
                                //  else check the answer. if correct ; that player is correct. else broadcast that message.
                            } else {
                                ServerLog.error("Player selecting word while server is not request!.");
                            }
                        } else if (currentWork.PacketId == Packet.PacketID.C2S_HeartBeat) {
                            C2S_HeartBeat gamePacket = (C2S_HeartBeat) currentWork;
                        } else if (currentWork.PacketId == Packet.PacketID.C2S_RequestUpdatePlayers) { // update all players
                            C2S_RequestUpdatePlayers gamePacket = (C2S_RequestUpdatePlayers) currentWork;
                        } else if (currentWork.PacketId == Packet.PacketID.C2S_RequestUpdateProfile) { // update only his/her profile
                            C2S_RequestUpdateProfile gamePacket = (C2S_RequestUpdateProfile) currentWork;
                        } else if (currentWork.PacketId == Packet.PacketID.C2S_RequestUpdateWhiteBoard) {
                            C2S_RequestUpdateWhiteBoard gamePacket = (C2S_RequestUpdateWhiteBoard) currentWork;
                        } else if (currentWork.PacketId == Packet.PacketID.C2S_SelectWord) { // drawing player selected word!
                            C2S_SelectWord gamePacket = (C2S_SelectWord) currentWork;

                            // todo : another check if player is drawer. if not then ignore it.

                            if (this.gameServer.getCurrentGameStatus() == GameServerStatus.gameStatus.GAME_WAITING_WORD) {
                                this.gameServer.setCurrentGameStatus(GameServerStatus.gameStatus.GAME_PLAYING);
                            } else {
                                ServerLog.error("Player selecting word while server is not require!.");
                            }
                        } else if (currentWork.PacketId == Packet.PacketID.C2S_UpdateWhiteBoard) { // drawing player update his/her white board
                            C2S_UpdateWhiteBoard gamePacket = (C2S_UpdateWhiteBoard) currentWork;
                        }


                        // more packet to add above
                        else {
                            ServerLog.error("Invalid packet from client!");
                        }
                    } else { // no this is not real player
                        ServerLog.error("Incorrect player income packet!");
                    }
                }
            }
            else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
