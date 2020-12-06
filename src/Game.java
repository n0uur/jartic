import GameClient.GameClient;
import GameClient.Model.GameClientStatus;
import GameServer.GameServer;

import javax.swing.*;

public class Game implements Runnable {
    public static enum GameState {
        MAIN_MENU,
        GAME_PLAYING,
        NEED_EXIT
    }

    private GameServer gameServer;
    private GameClient gameClient;
    private GameState gameState;

    public Game() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new GameMainMenu(this);

        this.gameState = GameState.MAIN_MENU;
    }

    public void main_loop() {

    }

    public void startGameServer() {
        this.gameServer = new GameServer();
    }

    public void startGameClient() {
        this.gameClient = new GameClient();
    }

    @Override
    public void run() {
        this.main_loop();
    }
}
