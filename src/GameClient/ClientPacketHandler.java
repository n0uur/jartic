package GameClient;

import GameClient.Model.GameClientStatus;
import GameClient.Model.LocalPlayerData;
import Shared.Logger.GameLog;
import Shared.Model.GamePacket.C2S_HeartBeat;
import Shared.Model.GamePacket.Packet;
import Shared.Model.GamePacket.S2C_AcceptJoinGameRequest;
import GameServer.Model.ServerPacket;

import javax.swing.*;
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
