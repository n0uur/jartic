package GameClient.ViewComponents;

import GameClient.GameClientView;

import javax.swing.*;
import java.awt.*;

public class CanvasPanel extends JPanel {
    private GameClientView view;

    public CanvasPanel(GameClientView view) {
        setBackground(Color.WHITE);
        this.view = view;
        addMouseListener(this.view.getGameClient());
        addMouseMotionListener(this.view.getGameClient());
    }
    @Override
    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            if (this.getMousePosition() != null && view.getGameClient().isErasing())
                g.drawRect(this.getMousePosition().x - 10, this.getMousePosition().y - 10, 20, 20);
            else if (this.getMousePosition() != null) {
                g.fillOval(this.getMousePosition().x - 5, this.getMousePosition().y - 5, 10, 10);
            }
            for (int i = 0; i < 928; i++) {
                for (int j = 0; j < 424; j++) {
                    if (this.view.getGameClient().getPoints()[i][j] == 1)
                        g.fillOval(i, j, 10, 10);
                }
            }
        }catch (NullPointerException Ignored){}
    }
}
