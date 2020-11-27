import Shared.Logger.GameLog;

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

        JPanel joinTopPanel = new JPanel();
        joinTopPanel.setLayout(new GridLayout(1, 2));

        this.ipTextField = new JTextField();
        this.ipLabel = new JLabel("Host's IP :");

        joinTopPanel.add(this.ipLabel);
        joinTopPanel.add(this.ipTextField);

        JPanel joinBottomPanel = new JPanel();
        joinBottomPanel.setLayout(new FlowLayout());
        this.backFromJoinButton = new JButton("Back");
        this.backFromJoinButton.addActionListener(gameMainMenuController);
        this.connectButton = new JButton("Connect");
        this.connectButton.addActionListener(gameMainMenuController);

        joinBottomPanel.add(backFromJoinButton);
        joinBottomPanel.add(connectButton);

        this.joinPanel.add(joinTopPanel, BorderLayout.NORTH);
        this.joinPanel.add(joinBottomPanel);

        this.mainMenuFrame.setLayout(new BorderLayout());
        this.mainMenuFrame.add(this.topPanel, BorderLayout.NORTH);

        this.updateMainMenu();

        this.mainMenuFrame.setSize(400, 140);
        this.mainMenuFrame.setVisible(true);
        this.mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void updateMainMenu() {
        if(this.isShowingJoinMenu) {
            this.mainMenuFrame.remove(this.mainPanel);
            this.mainMenuFrame.add(this.joinPanel, BorderLayout.CENTER);
        }
        else {
            this.mainMenuFrame.remove(this.joinPanel);
            this.mainMenuFrame.add(this.mainPanel, BorderLayout.CENTER);
        }
        this.mainMenuFrame.revalidate();
        this.mainMenuFrame.repaint();
    }

    public void setShowingJoinMenu(boolean showing) {
        GameLog.Log("updating join game menu.. " + showing);
        this.isShowingJoinMenu = showing;
        this.updateMainMenu();
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
