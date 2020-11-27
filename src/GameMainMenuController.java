import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMainMenuController implements ActionListener {

    private GameMainMenuView gameMainMenuView;

    public GameMainMenuController() {
        this.gameMainMenuView = new GameMainMenuView(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.gameMainMenuView.getJoinGameButton())) {
            this.gameMainMenuView.setShowingJoinMenu(true);
        }
        else if(e.getSource().equals(this.gameMainMenuView.getBackFromJoinButton())) {
            this.gameMainMenuView.setShowingJoinMenu(false);
        }
    }
}
