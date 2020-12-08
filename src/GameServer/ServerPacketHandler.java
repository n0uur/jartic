package GameServer;

import GameClient.Model.ClientPacket;
import GameServer.Model.ServerPlayer;
import Shared.Logger.ServerLog;
import Shared.Model.GameConfig;
import Shared.GamePacket.*;
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
                        responsePacket.gameStatus = Shared.Model.GameServerStatus.GAME_PLAYING;
                        responsePacket.playerProfile = newServerPlayer.getPlayerProfile();

                        S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                        chatPacket.message = gamePacket.playerName + " has join the game! <3";
                        chatPacket.flag = S2C_ChatMessage.messageFlag.MESSAGE_SUCCESS;
                        chatPacket.broadcastToClient();
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

                            if(gamePacket.message.length() > 0) {
                                if (this.gameServer.getCurrentGameStatus() == GameServerStatus.GAME_PLAYING) {
                                    if(!packetServerPlayer.getPlayerProfile().isDrawing()) {
                                        if(packetServerPlayer.getPlayerProfile().isCorrected()) {
                                            S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                                            chatPacket.message = "[" + packetServerPlayer.getPlayerProfile().getName() + "] " + gamePacket.message;
                                            chatPacket.flag = S2C_ChatMessage.messageFlag.MESSAGE_NORMAL;
                                            chatPacket.broadcastToClient();
                                        }
                                        else {
                                            if(this.gameServer.getDrawingWord().toLowerCase().equals(gamePacket.message.toLowerCase())) {
                                                S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                                                chatPacket.message = gamePacket.message +
                                                        " is the correct answer! ("+
                                                        packetServerPlayer.getPlayerProfile().getScore() +
                                                        " + "+ this.gameServer.getAnswerScore() +")";
                                                chatPacket.flag = S2C_ChatMessage.messageFlag.MESSAGE_SUCCESS;
                                                chatPacket.sendToClient(packetServerPlayer.getPeerId());

                                                chatPacket = new S2C_ChatMessage();
                                                chatPacket.message = "[Server] "+ packetServerPlayer.getPlayerProfile().getName() + " has guess the correct answer!";
                                                chatPacket.flag = S2C_ChatMessage.messageFlag.MESSAGE_NORMAL;
                                                chatPacket.broadcastToClient();

                                                packetServerPlayer.getPlayerProfile().setCorrected(true);

                                                ServerLog.warn((this.gameServer.getDrawingTimeLeft() / 3) + "");

                                                this.gameServer.getDrawingPlayer().getPlayerProfile().setScore(
                                                        this.gameServer.getDrawingPlayer().getPlayerProfile().getScore() + (this.gameServer.getDrawingTimeLeft() / 9)
                                                );

                                                packetServerPlayer.getPlayerProfile().setScore(packetServerPlayer.getPlayerProfile().getScore() + this.gameServer.getAnswerScore());
                                                this.gameServer.setAnswerScore(this.gameServer.getAnswerScore() - 1);
                                            }
                                            else {
                                                S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                                                chatPacket.message = "[" + packetServerPlayer.getPlayerProfile().getName() + "] " + gamePacket.message;
                                                chatPacket.flag = S2C_ChatMessage.messageFlag.MESSAGE_NORMAL;
                                                chatPacket.broadcastToClient();
                                            }
                                        }
                                    }

                                    else { // drawer type something...
                                        S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                                        chatPacket.message = "[Server] You don't have permission to type anything at this time. JUST DRAW!";
                                        chatPacket.flag = S2C_ChatMessage.messageFlag.MESSAGE_DANGER;
                                        chatPacket.sendToClient(packetServerPlayer.getPeerId());
                                    }

                                } else {
                                    S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                                    chatPacket.message = "[" + packetServerPlayer.getPlayerProfile().getName() + "] " + gamePacket.message;
                                    chatPacket.flag = S2C_ChatMessage.messageFlag.MESSAGE_NORMAL;
                                    chatPacket.broadcastToClient();
                                }
                            }

                        }
                        else if (currentWork.PacketId == Packet.PacketID.C2S_HeartBeat) {
                            C2S_HeartBeat gamePacket = (C2S_HeartBeat) currentWork;
                        }
                        else if (currentWork.PacketId == Packet.PacketID.C2S_RequestUpdatePlayers) { // update all players
                            C2S_RequestUpdatePlayers gamePacket = (C2S_RequestUpdatePlayers) currentWork;
                        }
                        else if (currentWork.PacketId == Packet.PacketID.C2S_RequestUpdateProfile) { // update only his/her profile
                            C2S_RequestUpdateProfile gamePacket = (C2S_RequestUpdateProfile) currentWork;
                        }
                        else if (currentWork.PacketId == Packet.PacketID.C2S_RequestUpdateWhiteBoard) {
                            C2S_RequestUpdateWhiteBoard gamePacket = (C2S_RequestUpdateWhiteBoard) currentWork;
                        }
                        else if (currentWork.PacketId == Packet.PacketID.C2S_SelectWord) { // drawing player selected word!

                            C2S_SelectWord gamePacket = (C2S_SelectWord) currentWork;

                            // todo : another check if player is drawer. if not then ignore it.

                            if (this.gameServer.getCurrentGameStatus() == GameServerStatus.GAME_WAITING_WORD) {

                                this.gameServer.setDrawingWord(gamePacket.word);
                                this.gameServer.setStartWaitingWord(false);
                                this.gameServer.setCurrentGameStatus(GameServerStatus.GAME_PLAYING);

                                ServerLog.log("Player selected : " + gamePacket.word);

                                S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                                chatPacket.message = "[Server] " + this.gameServer.getDrawingPlayer().getPlayerProfile().getName() + " has selected word! LET'S GO!";
                                chatPacket.broadcastToClient();

                            } else {
                                ServerLog.error("Player selecting word while server is not require!.");
                            }
                        }
                        else if (currentWork.PacketId == Packet.PacketID.C2S_UpdateWhiteBoard) { // drawing player update his/her white board
                            C2S_UpdateWhiteBoard gamePacket = (C2S_UpdateWhiteBoard) currentWork;

//                            if(System.currentTimeMillis() - this.gameServer.getLastBroadcastWhiteboard() > 500) {
                                this.gameServer.setLastBroadcastWhiteboard(System.currentTimeMillis());

                                S2C_UpdateWhiteBoard whiteBoardPacket = new S2C_UpdateWhiteBoard();
                                whiteBoardPacket.x = gamePacket.x;
                                whiteBoardPacket.y = gamePacket.y;
                                whiteBoardPacket.value = gamePacket.value;
                                whiteBoardPacket.broadcastToClient();
//                            }
                        }
                        else if (currentWork.PacketId == Packet.PacketID.C2S_GameClose) {

                            S2C_ChatMessage leftMessage = new S2C_ChatMessage();
                            leftMessage.flag = S2C_ChatMessage.messageFlag.MESSAGE_DANGER;
                            leftMessage.message = packetServerPlayer.getPlayerProfile().getName() + " has left the game.";
                            leftMessage.broadcastToClient();

                            packetServerPlayer.remove();
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
