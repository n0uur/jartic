package GameClient;

import GameClient.Model.ClientPlayer;
import GameClient.Model.GameClientStatus;
import GameClient.Model.LocalPlayerData;
import GameClient.UI.SelectWord;
import GameServer.Model.ServerPacket;
import Shared.Model.GamePacket.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

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

//                    GameLog.Log("Hay! I got some job! [" + currentWork.PacketId + "]");

                    if(currentWork.PacketId == Packet.PacketID.S2C_AcceptJoinGameRequest) {

                        S2C_AcceptJoinGameRequest responsePacket = (S2C_AcceptJoinGameRequest) currentWork;

                        if(responsePacket.isRoomFull) {
                            JOptionPane.showMessageDialog(null, "This room is already full.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            LocalPlayerData.setPlayerProfile(responsePacket.playerProfile);
                            LocalPlayerData.setToken(responsePacket.playerToken);

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

                        // todo : set game state
                        gameClient.setGameServerState(serverData.gameServerStatus);

                    }
                    else if(currentWork.PacketId == Packet.PacketID.S2C_ChatMessage) {
                        S2C_ChatMessage chatPacket = (S2C_ChatMessage) currentWork;
                        GameClientView view = this.gameClient.getGameClientView();
                        view.getChatLogMsg().setText(view.getChatLogMsg().getText() + chatPacket.message + '\n');
                        view.scrollToBottom();
                    }
                    else if(currentWork.PacketId == Packet.PacketID.S2C_RequestWord) {
                        S2C_RequestWord wordPacket = (S2C_RequestWord) currentWork;

                        // todo : show select word dialog and send back to server using C2S_SelectWord

                        new SelectWord(gameClient, wordPacket.words[0], wordPacket.words[1]);

                        //  wordPacket.words
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
