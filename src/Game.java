import GameServer.GameServerController;

public class Game {
    public static enum GameState {
        MAIN_MENU,
        GAME_PLAYING,
        NEED_EXIT
    }

    public Game() {
        new GameServerController();
        new GameMainMenuController();
    }
}
