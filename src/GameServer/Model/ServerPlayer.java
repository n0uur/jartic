package GameServer.Model;

import Shared.Model.PlayerProfile;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;

public class ServerPlayer {

    //////////////////////////////////////////////

    private static int _peerCount;
    private static ArrayList<ServerPlayer> players;

    private static void addPlayer(ServerPlayer serverPlayer) {
        if (players == null) {
            players = new ArrayList<ServerPlayer>();
        }
        players.add(serverPlayer);
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

    public static ServerPlayer getPlayer(String token) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).getPlayerToken().equals(token)) {
                return players.get(i);
            }
        }
        return null;
    }

    public static ServerPlayer getPlayer(int peerId) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).peerId == peerId) {
                return players.get(i);
            }
        }
        return null;
    }

    public static ArrayList<ServerPlayer> getPlayers() {
        return players;
    }

    //////////////////////////////////////////////

    private PlayerProfile playerProfile;
    private String playerToken;
    private int peerId;

    private InetAddress playerIp;
    private int playerPort;

    public ServerPlayer(String name) {
        this.playerToken = UUID.randomUUID().toString();
        this.playerProfile = new PlayerProfile("name");
        this.peerId = _peerCount + 1;
        addPlayer(this);
    }

    public ServerPlayer() {
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

    public int getPeerId() {
        return this.peerId;
    }

    //////////////////////////////////////////////

}
