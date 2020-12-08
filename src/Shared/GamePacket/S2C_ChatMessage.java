package Shared.GamePacket;

import GameServer.Model.ServerPacket;

public class S2C_ChatMessage extends ServerPacket {

    public static enum messageFlag {
        MESSAGE_NORMAL,
        MESSAGE_WARNING,
        MESSAGE_SUCCESS,
        MESSAGE_DANGER
    }

    public String message;
    public messageFlag flag;
}
