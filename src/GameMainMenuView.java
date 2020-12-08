import GameClient.Model.LocalPlayerData;
import Shared.Logger.GameLog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    public GameMainMenuView(GameMainMenu gameMainMenu) {
        this.mainMenuFrame = new JFrame("Jartic - java gartic.io cloned!");

        this.topPanel = new JPanel();
        this.nameLabel = new JLabel("Enter your name :");

        if (LocalPlayerData.getPlayerName() != null) {
            this.nameTextField = new JTextField(LocalPlayerData.getPlayerName());
        } else
            this.nameTextField = new JTextField();

        this.topPanel.setLayout(new GridLayout(1, 2));
        this.topPanel.add(this.nameLabel);
        this.topPanel.add(this.nameTextField);
        this.topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.mainPanel = new JPanel();
        this.hostGameButton = new JButton("Host");
        this.hostGameButton.addActionListener(gameMainMenu);
        this.joinGameButton = new JButton("Join");
        this.joinGameButton.addActionListener(gameMainMenu);

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
        joinTopPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        JPanel joinBottomPanel = new JPanel();
        joinBottomPanel.setLayout(new FlowLayout());
        this.backFromJoinButton = new JButton("Back");
        this.backFromJoinButton.addActionListener(gameMainMenu);
        this.connectButton = new JButton("Connect");
        this.connectButton.addActionListener(gameMainMenu);

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
            this.mainMenuFrame.setSize(this.mainMenuFrame.getWidth(), 160);
        }
        else {
            this.mainMenuFrame.remove(this.joinPanel);
            this.mainMenuFrame.add(this.mainPanel, BorderLayout.CENTER);
            this.mainMenuFrame.setSize(this.mainMenuFrame.getWidth(), 140);
        }
        this.mainMenuFrame.revalidate();
        this.mainMenuFrame.repaint();
    }

    public void setShowingJoinMenu(boolean showing) {
        GameLog.log("updating join game menu.. " + showing);
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

    public JFrame getMainMenuFrame() {
        return mainMenuFrame;
    }
}
