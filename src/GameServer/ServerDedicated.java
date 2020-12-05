package GameServer;

public class ServerDedicated {
    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        while(true) {
            gameServer.update();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
