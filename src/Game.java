import GameClient.GameClient;
import GameClient.Model.GameClientStatus;
import GameServer.GameServer;

public class Game {
    public static enum GameState {
        MAIN_MENU,
        GAME_PLAYING,
        NEED_EXIT
    }

    private GameServer gameServer;
    private GameClient gameClient;
    private GameState gameState;

    public Game() {
        new GameMainMenu(this);

        this.gameState = GameState.MAIN_MENU;
    }

    public void main_loop() {
        while (gameState != GameState.NEED_EXIT) {

            if(this.gameServer != null) {
                this.gameServer.update();
            }

            if(this.gameClient != null) {
                this.gameClient.update();

                if(this.gameClient.getGameStatus() == GameClientStatus.GAME_NEED_TO_QUIT) {
                    this.gameState = GameState.NEED_EXIT;
                }
            }

            try {
                Thread.sleep(30); // around 30 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(this.gameServer != null) {
            // todo : told all client, server is closing...
            this.gameServer.destroy();
        }

        System.exit(0);
    }

    public void startGameServer() {
        this.gameServer = new GameServer();
    }

    public void startGameClient() {
        this.gameClient = new GameClient();
    }
}
