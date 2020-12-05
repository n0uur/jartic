package Shared.Model.GamePacket;

import Shared.Model.GameServer;
import Shared.Model.PlayerProfile;

public class S2C_AcceptJoinGameRequest extends ServerPacket {
    public PlayerProfile playerProfile;
    public GameServer.gameStatus gameStatus;
    public String playerToken;
    public boolean isRoomFull;

// todo.. + public WhiteBoard whiteBoard;

}
