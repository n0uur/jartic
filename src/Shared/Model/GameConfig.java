package Shared.Model;

public class GameConfig {
    public static final int SERVER_GAME_PORT = 20770;
    public static final int MAX_PLAYER = 9;

    public static String serverAddress;

    public static String getServerAddress() {
        return serverAddress;
    }

    public static void setServerAddress(String serverAddress) {
        GameConfig.serverAddress = serverAddress;
    }
}
