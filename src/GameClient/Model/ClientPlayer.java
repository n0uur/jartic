package GameClient.Model;

import Shared.Logger.GameLog;
import Shared.Model.PlayerProfile;

import java.util.ArrayList;
import java.util.Iterator;

public class ClientPlayer {
    public static ArrayList<ClientPlayer> players;

    public static ArrayList<ClientPlayer> getPlayers() {
        if(players == null) {
            players = new ArrayList<>();
        }
        return players;
    }

    public static ClientPlayer getPlayer(int id) {
        for (int i = 0; i < getPlayers().size(); i++) {
            ClientPlayer player = players.get(i);
            if (player.getPlayerProfile().getId() == id) {
                return player;
            }
        }
        return null;
    }

    public synchronized static void addPlayer(ClientPlayer player) {
        players.add(player);
    }

    public synchronized static void updatePlayers(ArrayList<PlayerProfile> newPlayers) {

        players = new ArrayList<>();
        for(int i = 0; i < newPlayers.size(); i++) {
            PlayerProfile newPlayer = newPlayers.get(i);

            new ClientPlayer(newPlayer);
//            GameLog.log("updated player : " + newPlayer.getName() + " (" + newPlayer.getId() + ")");
        }


//        for(int i = 0; i < newPlayers.size(); i++) {
//            PlayerProfile newPlayer = newPlayers.get(i);
//
//            ClientPlayer localPlayer = ClientPlayer.getPlayer(newPlayer.getId());
//            if(localPlayer != null) { // have any player in local array
//                localPlayer.playerProfile.setScore(newPlayer.getScore());
//                localPlayer.playerProfile.isDrawing(newPlayer.isDrawing());
//
//                GameLog.log("Updated player : " + newPlayer.getName());
//            }
//            else { // new player appeared!
//                new ClientPlayer(newPlayer);
//                GameLog.log("New player in coming : " + newPlayer.getName() + " (" + newPlayer.getId() + ")");
//            }
//        }
//
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // check for player that removed from server..
//        Iterator<ClientPlayer> playersIterator = getPlayers().iterator();
//
//        while (playersIterator.hasNext()) {
//            ClientPlayer player = playersIterator.next();
//
//            for(int j = 0; j < newPlayers.size(); j++) {
//                if(player.getPlayerProfile().getId() == newPlayers.get(j).getId()) {
//                    break;
//                }
//                // player not found maybe his/her disconnected, just remove..
//                GameLog.log("removed " + player.getPlayerProfile().getName() + " ("+ player.getPlayerProfile().getId() +") bye :(");
//                playersIterator.remove();
//            }
//        }
    }

    ////////////////////////////////////////

    private PlayerProfile playerProfile;

    public ClientPlayer(PlayerProfile profile) {
        this.setPlayerProfile(profile);
        ClientPlayer.addPlayer(this);
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
