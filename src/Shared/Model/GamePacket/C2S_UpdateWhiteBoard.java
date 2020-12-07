package Shared.Model.GamePacket;

import GameClient.Model.ClientPacket;

public class C2S_UpdateWhiteBoard extends ClientPacket {
    public int x;
    public int y;
    public int value;
}
