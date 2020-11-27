import javax.swing.*;
import java.awt.*;

public class GameMainMenuView {
    private JFrame mainMenuFrame;

    private JPanel topPanel;
    private JPanel mainPanel;
    private JPanel joinPanel;

    private JLabel nameLabel;
    private JLabel ipLabel;
    private JTextField nameTextField;
    private JTextField ipTextField;

    private JButton hostGameButton;
    private JButton joinGameButton;
    private JButton backFromJoinButton;
    private JButton connectButton;

    private boolean isShowingJoinMenu;

    public GameMainMenuView(GameMainMenuController gameMainMenuController) {
        this.mainMenuFrame = new JFrame("Jartic - java gartic.io cloned!");

        this.topPanel = new JPanel();
        this.nameLabel = new JLabel("Enter your name :");
        this.nameTextField = new JTextField();

        this.topPanel.setLayout(new GridLayout(1, 2));
        this.topPanel.add(this.nameLabel);
        this.topPanel.add(this.nameTextField);

        this.mainPanel = new JPanel();
        this.hostGameButton = new JButton("Host");
        this.hostGameButton.addActionListener(gameMainMenuController);
        this.joinGameButton = new JButton("Join");
        this.joinGameButton.addActionListener(gameMainMenuController);

        this.mainPanel.setLayout(new FlowLayout());
        this.mainPanel.add(this.hostGameButton);
        this.mainPanel.add(this.joinGameButton);

        this.joinPanel = new JPanel();
        this.joinPanel.setLayout(new BorderLayout());

        this.ipTextField = new JTextField();
        this.ipLabel = new JLabel("Host's IP :");
        this.backFromJoinButton = new JButton("Back");
        this.backFromJoinButton.addActionListener(gameMainMenuController);
        this.connectButton = new JButton("Connect");
        this.backFromJoinButton.addActionListener(gameMainMenuController);

        this.joinPanel.add(ipLabel, BorderLayout.NORTH);
        this.joinPanel.add(ipTextField, BorderLayout.NORTH);
        this.joinPanel.add(backFromJoinButton);
        this.joinPanel.add(connectButton);

        this.mainMenuFrame.setLayout(new BorderLayout());
        this.mainMenuFrame.add(this.topPanel, BorderLayout.NORTH);
        this.mainMenuFrame.add(this.mainPanel);

        this.mainMenuFrame.pack();
        this.mainMenuFrame.setVisible(true);
        this.mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setShowingJoinMenu(boolean showing) {
        this.isShowingJoinMenu = showing;
    }

    public JTextField getNameTextField() {
        return nameTextField;
    }

    public JTextField getIpTextField() {
        return ipTextField;
    }

    public JButton getHostGameButton() {
        return hostGameButton;
    }

    public JButton getJoinGameButton() {
        return joinGameButton;
    }

    public JButton getBackFromJoinButton() {
        return backFromJoinButton;
    }

    public JButton getConnectButton() {
        return connectButton;
    }
}
