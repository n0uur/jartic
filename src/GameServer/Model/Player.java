package GameServer.Model;

import Shared.Model.PlayerProfile;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;

public class Player {

    //////////////////////////////////////////////

    private static int _peerCount;
    private static ArrayList<Player> players;

    private static void addPlayer(Player player) {
        if (players == null) {
            players = new ArrayList<Player>();
        }
        players.add(player);
        _peerCount += 1;
    }

    public static void removePlayer(String token) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).getPlayerToken().equals(token)) {
                players.remove(i);
                break;
            }
        }
    }

    public static void removePlayer(int i) {
        players.remove(i);
    }

    public static Player getPlayer(String token) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).getPlayerToken().equals(token)) {
                return players.get(i);
            }
        }
        return null;
    }

    public static Player getPlayer(int peerId) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).peerId == peerId) {
                return players.get(i);
            }
        }
        return null;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    //////////////////////////////////////////////

    private PlayerProfile playerProfile;
    private String playerToken;
    private int peerId;

    private InetAddress playerIp;
    private int playerPort;

    public Player(String name) {
        this.playerToken = UUID.randomUUID().toString();
        this.playerProfile = new PlayerProfile("name");
        this.peerId = _peerCount + 1;
        addPlayer(this);
    }

    public Player() {
        this("");
    }

    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }

    public String getPlayerToken() {
        return this.playerToken;
    }

    public void remove() {
        removePlayer(this.getPlayerToken());
    }

    public void setPlayerResponseAddress(InetAddress playerIp, int port) {
        this.playerIp = playerIp;
        this.playerPort = port;
    }

    public InetAddress getPlayerIp() {
        return playerIp;
    }

    public int getPlayerPort() {
        return playerPort;
    }

    //////////////////////////////////////////////

}
