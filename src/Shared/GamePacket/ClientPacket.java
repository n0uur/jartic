package Shared.GamePacket;

import Shared.CanSendAsPacket;
import Shared.Packet;

public abstract class ClientPacket extends Packet implements CanSendAsPacket {
    public void sendToServer() {
        // todo..
    }
}
