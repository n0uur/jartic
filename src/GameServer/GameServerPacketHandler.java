package GameServer;

import Shared.GamePacket.ClientPacket;
import Shared.GamePacket.Packet;

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
                    // todo..
                }
                else if (currentWork.PacketId == Packet.PacketID.C2S_ChatMessage){
                    // todo..
                }
            }
        }
    }
}
