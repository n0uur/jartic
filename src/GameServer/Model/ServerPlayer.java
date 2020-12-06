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

    public synchronized static ArrayList<ServerPlayer> getPlayers() {
        if(players == null) {
            players = new ArrayList<ServerPlayer>();
        }
        return players;
    }

    //////////////////////////////////////////////

    private PlayerProfile playerProfile;
    private String playerToken;
    private int peerId;

    private InetAddress playerIp;
    private int playerPort;

    private long lastResponse;
    private boolean isRequestedHeartBeat;

    public ServerPlayer(String name) {
        this.playerToken = UUID.randomUUID().toString();
        this.playerProfile = new PlayerProfile(name);
        this.peerId = _peerCount + 1;
        this.playerProfile.setId(this.peerId);
        this.response();
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
        players.remove(this);
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

    public long getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(long lastResponse) {
        this.lastResponse = lastResponse;
    }

    public boolean isRequestedHeartBeat() {
        return isRequestedHeartBeat;
    }

    public void isRequestedHeartBeat(boolean requestedHeartBeat) {
        isRequestedHeartBeat = requestedHeartBeat;
    }

    public void response() {
        this.setLastResponse(System.currentTimeMillis());
    }

    //////////////////////////////////////////////

}
