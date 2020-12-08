package GameClient;

import GameClient.Model.ClientPlayer;
import GameClient.Model.GameClientStatus;
import GameClient.Model.LocalPlayerData;
import Shared.GamePacket.ServerPacket;
import Shared.Logger.GameLog;
import Shared.GamePacket.*;
import Shared.Model.GameServerStatus;

import javax.swing.*;
import java.util.ArrayList;

public class ClientPacketHandler implements Runnable {

    GameClient gameClient;

    private boolean isDestroy;

    public ClientPacketHandler (GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public void destroy() {
        this.isDestroy = true;
    }

    @Override
    public void run() {
        while(!isDestroy) {
            ArrayList<ServerPacket> workQueue = gameClient.getServerPackets();
            if(workQueue.size() > 0) {
                synchronized (this) {
                    ServerPacket currentWork =  workQueue.remove(0);

                    this.gameClient.serverResponse();

//                    GameLog.Log("Hay! I got some job! [" + currentWork.PacketId + "]");

                    if(currentWork == null) {
                        continue;
                    }

                    if(currentWork.PacketId == Packet.PacketID.S2C_AcceptJoinGameRequest) {

                        S2C_AcceptJoinGameRequest responsePacket = (S2C_AcceptJoinGameRequest) currentWork;

                        if(responsePacket.isRoomFull) {
                            JOptionPane.showMessageDialog(null, "This room is already full.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            LocalPlayerData.setPlayerProfile(responsePacket.playerProfile);
                            LocalPlayerData.setToken(responsePacket.playerToken);
                            LocalPlayerData.setId(responsePacket.playerProfile.getId());

                            GameLog.warn("I'm " + LocalPlayerData.getPlayerProfile().getName() + " ("+ LocalPlayerData.getId() +")");

                            this.gameClient.setGameStatus(GameClientStatus.GAME_WAITING);
                        }
                    }
                    else if(currentWork.PacketId == Packet.PacketID.S2C_RequestHeartBeat) {

                        C2S_HeartBeat heartBeat = new C2S_HeartBeat();
                        heartBeat.sendToServer();

                    }
                    else if(currentWork.PacketId == Packet.PacketID.S2C_UpdateServerData) {

                        S2C_UpdateServerData serverData = (S2C_UpdateServerData) currentWork;

                        ClientPlayer.updatePlayers(serverData.playersProfile);

                        gameClient.setHintWord(serverData.hintWord);
                        gameClient.setRealWord(serverData.realWord);
                        gameClient.setTimer(serverData.timeLeftInSeconds);

                        gameClient.isDrawer(serverData.drawerId == LocalPlayerData.getId());

                        gameClient.setGameServerState(serverData.gameServerStatus);

                        if(serverData.gameServerStatus == GameServerStatus.GAME_NEXT_PLAYER) {
                            gameClient.destroySelectWord();
                            gameClient.getDrawingBoard().clear();
                            gameClient.getGameClientView().getCanvasPanel().repaint();
                        }

                        gameClient.getGameClientView().drawPlayers();

                    }
                    else if(currentWork.PacketId == Packet.PacketID.S2C_ChatMessage) {
                        S2C_ChatMessage chatPacket = (S2C_ChatMessage) currentWork;
                        GameClientView view = this.gameClient.getGameClientView();
                        view.getChatLogMsg().setText(view.getChatLogMsg().getText() + chatPacket.message + '\n');
                        view.scrollToBottom();
                    }
                    else if(currentWork.PacketId == Packet.PacketID.S2C_RequestWord) {
                        S2C_RequestWord wordPacket = (S2C_RequestWord) currentWork;

                        gameClient.newSelectWord(wordPacket.words[0], wordPacket.words[1]);
                    }
                    else if(currentWork.PacketId == Packet.PacketID.S2C_UpdateWhiteBoard) {
                        S2C_UpdateWhiteBoard whiteBoardPacket = (S2C_UpdateWhiteBoard) currentWork;

                        if(whiteBoardPacket.needToClear) {
                            this.gameClient.getDrawingBoard().clear();
                        }
                        else {
                            this.gameClient.getDrawingBoard().update(whiteBoardPacket.x, whiteBoardPacket.y, whiteBoardPacket.value);
                        }

                        this.gameClient.getGameClientView().getCanvasPanel().repaint();
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
