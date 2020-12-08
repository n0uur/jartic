package Shared.GamePacket;

import Shared.Model.GameServerStatus;
import Shared.Model.PlayerProfile;

import java.util.ArrayList;

public class S2C_UpdateServerData extends ServerPacket {
    // todo : add what do you want to update

    public ArrayList<PlayerProfile> playersProfile;
//    public String drawingBoard;
    public GameServerStatus gameServerStatus;
    public String hintWord;
    public String realWord;
    public int timeLeftInSeconds;
    public int drawerId;

}
