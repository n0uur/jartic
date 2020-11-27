import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMainMenuController implements ActionListener {

    private GameMainMenuView gameMainMenuView;

    public GameMainMenuController() {
        this.gameMainMenuView = new GameMainMenuView(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
