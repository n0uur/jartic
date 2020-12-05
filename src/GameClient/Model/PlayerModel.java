package GameClient.Model;

import Shared.Model.PlayerProfile;

import java.util.ArrayList;

public class PlayerModel {
    public static boolean isLocalPlayer = false;
    public static ArrayList<PlayerProfile> playersProfile;

    public PlayerModel(ArrayList<PlayerProfile> playersProfile) {
        PlayerModel.playersProfile = playersProfile;
    }

    public static boolean isLocalPlayer() {
        return isLocalPlayer;
    }

    public static void setLocalPlayer(boolean localPlayer) {
        isLocalPlayer = localPlayer;
    }

    public static ArrayList<PlayerProfile> getPlayers() {
        return playersProfile;
    }

    public static PlayerProfile getPlayer(int id) {
        for (int i = 0; i < playersProfile.size(); i++) {
            if (id == playersProfile.get(i).getId()) {
                return playersProfile.get(i);
            }
        }
        return null;
    }



}
