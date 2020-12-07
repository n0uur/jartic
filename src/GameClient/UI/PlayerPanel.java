package GameClient.UI;

import GameClient.GameClientView;
import GameClient.Model.ClientPlayer;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
    private GameClientView view;
    private JLabel player;

    public PlayerPanel(GameClientView view) {
        this.view = view;
        this.player = new JLabel("Player");
        this.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        this.setPreferredSize(new java.awt.Dimension(320, 60));
        this.setLayout(new java.awt.GridLayout(12, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.removeAll();
        this.add(player);

        ClientPlayer.getPlayers().forEach((player) -> {
            this.add(new PlayerComponent(player.getPlayerProfile().getName(), player.getPlayerProfile().getScore()).getPlayerPanel());
        });
    }
}
