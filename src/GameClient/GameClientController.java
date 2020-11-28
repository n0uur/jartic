package GameClient;

public class GameClientController implements InteractionListener{
    private GameClientView gameClientView;

    public GameClientController() {
        gameClientView = new GameClientView(this);
    }

    public void interactionPerformed(Object e) {

    }
}
