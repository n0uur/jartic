package GameServer;

public class GameServerPacketHandler implements Runnable {

    private GameServerController gameServerController;

    public GameServerPacketHandler(GameServerController gameServerController) {
        this.gameServerController = gameServerController;
    }

    @Override
    public void run() {

    }
}
