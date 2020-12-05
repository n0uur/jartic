import GameClient.GameClient;
import GameClient.Model.LocalPlayerData;
import Shared.Model.GamePacket.C2S_JoinGame;
import Shared.Model.GameConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMainMenu implements ActionListener {

    private GameMainMenuView gameMainMenuView;

    private Game mainGame;

    public GameMainMenu(Game game) {
        this.mainGame = game;
        this.gameMainMenuView = new GameMainMenuView(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        LocalPlayerData.setPlayerName(this.gameMainMenuView.getNameTextField().getText());

        if(e.getSource().equals(this.gameMainMenuView.getJoinGameButton())) {
            this.gameMainMenuView.setShowingJoinMenu(true);
        }
        else if(e.getSource().equals(this.gameMainMenuView.getBackFromJoinButton())) {
            this.gameMainMenuView.setShowingJoinMenu(false);
        }
        else if (e.getSource().equals(this.gameMainMenuView.getHostGameButton())) {

            GameConfig.setServerAddress("localhost");

            this.mainGame.startGameServer();
            this.mainGame.startGameClient();

            this.gameMainMenuView.getMainMenuFrame().dispose();

        }
        else if (e.getSource().equals(this.gameMainMenuView.getConnectButton())) {

            String serverIp = this.gameMainMenuView.getIpTextField().getText();

            GameConfig.setServerAddress(serverIp);

            this.mainGame.startGameClient();

            this.gameMainMenuView.getMainMenuFrame().dispose();
        }
    }
}
