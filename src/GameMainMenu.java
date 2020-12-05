import GameClient.LocalSave.LocalPlayerName;
import Shared.Model.GamePacket.C2S_JoinGame;
import Shared.Model.GameConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMainMenu implements ActionListener {

    private GameMainMenuView gameMainMenuView;

    public GameMainMenu() {
        this.gameMainMenuView = new GameMainMenuView(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        LocalPlayerName.setPlayerName(this.gameMainMenuView.getNameTextField().getText());

        if(e.getSource().equals(this.gameMainMenuView.getJoinGameButton())) {
            this.gameMainMenuView.setShowingJoinMenu(true);
        }
        else if(e.getSource().equals(this.gameMainMenuView.getBackFromJoinButton())) {
            this.gameMainMenuView.setShowingJoinMenu(false);
        }
        else if (e.getSource().equals(this.gameMainMenuView.getHostGameButton())) {

        }
        else if (e.getSource().equals(this.gameMainMenuView.getConnectButton())) {

            String serverIp = this.gameMainMenuView.getIpTextField().getText();

            GameConfig.setServerAddress(serverIp);

            C2S_JoinGame joinGamePacket = new C2S_JoinGame();
            joinGamePacket.playerName = LocalPlayerName.getPlayerName();
            joinGamePacket.sendToServer();

//            this.gameMainMenuView.getMainMenuFrame().dispose();
        }
    }
}
