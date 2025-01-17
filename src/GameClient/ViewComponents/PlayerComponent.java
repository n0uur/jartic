package GameClient.ViewComponents;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerComponent {
    private JPanel playerPanel, playerInfoPanel;
    private JLabel playerName, playerScoreLabel, playerImage;
    private ImageIcon playerIcon;
    private Image image;
    private BufferedImage bfImg;
    private Border padding;
    private int playerScore;
    private int id;

    public PlayerComponent(String playerName, int playerScore, int id) {
        this.id = id;
        this.playerScore = playerScore;
        this.playerPanel = new JPanel();
        this.playerInfoPanel = new JPanel();
        this.playerName = new JLabel(playerName);
        this.playerScoreLabel = new JLabel("Score : " + playerScore);
        try {
            bfImg = ImageIO.read(new File("resource/playerIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.image = bfImg.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        this.playerIcon = new ImageIcon(this.image);
        this.playerImage = new JLabel(this.playerIcon);
        this.playerName.setFont(FontManager.getFont().deriveFont(24f));
        this.playerScoreLabel.setFont(FontManager.getFont().deriveFont(18f));

        this.padding = BorderFactory.createEmptyBorder(5, 20, 5, 10);
        this.playerInfoPanel.setBorder(this.padding);

        this.playerPanel.setLayout(new BorderLayout());
        this.playerInfoPanel.setLayout(new GridLayout(2, 1));
        this.playerInfoPanel.add(this.playerName);
        this.playerInfoPanel.add(this.playerScoreLabel);
        this.playerPanel.add(this.playerImage, BorderLayout.WEST);
        this.playerPanel.add(this.playerInfoPanel, BorderLayout.CENTER);

        this.playerPanel.setPreferredSize(new Dimension (290, 70));

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void update() {
        this.playerScoreLabel.setText("Score : " + playerScore);
    }

    public void setPlayerName(String playerName) {
        this.playerName.setText(playerName);
    }

    public JPanel getPlayerPanel() {
        return playerPanel;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
        this.playerScoreLabel.setText("Score : " + playerScore);
    }
}
