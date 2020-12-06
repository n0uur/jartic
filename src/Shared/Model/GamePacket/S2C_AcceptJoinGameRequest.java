package Shared.Model.GamePacket;

import GameServer.Model.ServerPacket;
import Shared.Model.GameServerStatus;
import Shared.Model.PlayerProfile;

public class S2C_AcceptJoinGameRequest extends ServerPacket {
    public PlayerProfile playerProfile;
    public GameServerStatus gameStatus;
    public String playerToken;
    public boolean isRoomFull;

// todo.. + public WhiteBoard whiteBoard;

}
