package Shared.GamePacket;

import Shared.CanSendAsPacket;

public abstract class ClientPacket extends Packet implements CanSendAsPacket {
    public void sendToServer() {
        // todo..
    }
}
