package Shared.GamePacket;

import GameServer.Model.ServerPacket;

public class S2C_UpdateWhiteBoard extends ServerPacket {
    public int x;
    public int y;
    public int value;
    public boolean needToClear;
}
