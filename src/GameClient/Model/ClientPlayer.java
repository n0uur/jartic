package GameClient.Model;

import Shared.Model.PlayerProfile;

import java.util.ArrayList;

public class ClientPlayer {
    public static ArrayList<ClientPlayer> players;

    public static ArrayList<ClientPlayer> getPlayers() {
        return players;
    }

    public static ClientPlayer getPlayer(int id) {
        for (int i = 0; i < players.size(); i++) {
            if (id == players.get(i).getPlayerProfile().getId()) {
                return players.get(i);
            }
        }
        return null;
    }

    ////////////////////////////////////////

    private PlayerProfile playerProfile;

    public ClientPlayer(PlayerProfile profile) {
        this.setPlayerProfile(profile);
    }

    public boolean isLocalPlayer() {
        return LocalPlayerData.getId() == this.getPlayerProfile().getId();
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public void setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }
}
