package GameServer;

public class ServerMain {
    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        while(true) {
            gameServer.update();
        }
    }
}
