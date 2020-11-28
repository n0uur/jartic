package Shared.GamePacket;

import Shared.CanSendAsPacket;

public abstract class ServerPacket extends Packet implements CanSendAsPacket {

    public void SendToClient(int peerId) {
        // todo..
    }
}
