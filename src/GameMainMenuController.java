import GameClient.LocalSave.LocalPlayerName;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMainMenuController implements ActionListener {

    private GameMainMenuView gameMainMenuView;

    public GameMainMenuController() {
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
            String ip = this.gameMainMenuView.getIpTextField().getText();
            String name = this.gameMainMenuView.getNameTextField().getText();
            this.gameMainMenuView.getMainMenuFrame().dispose();
        }
    }
}
