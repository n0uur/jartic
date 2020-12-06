package GameClient.UI;

import GameClient.GameClient;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SelectWord {
    private JFrame selectWordFrame;
    private JButton word1, word2;
    private JLabel selectWord;
    private JPanel buttonPanel;

    private GameClient gameClient;

    public SelectWord(GameClient controller, String w1, String w2) {
        gameClient = controller;

        selectWordFrame = new JFrame("Select Word");
        word1 = new JButton(w1);
        word2 = new JButton(w2);
        selectWord = new JLabel("Please Select Word");
        buttonPanel = new JPanel();

        selectWordFrame.setLayout(new BorderLayout());
        buttonPanel.setLayout(new GridLayout(1, 2));
        selectWordFrame.setResizable(false);

        Dimension wordD = new Dimension();
        wordD.width = 100;
        wordD.height = 40;

        Border b = BorderFactory.createEmptyBorder(5, 80, 5, 5);
        selectWord.setBorder(b);

        selectWord.setPreferredSize(wordD);

        buttonPanel.add(word1);
        buttonPanel.add(word2);

        word1.setFont(FontManager.getFont().deriveFont(32));
        word2.setFont(FontManager.getFont().deriveFont(32));
        selectWord.setFont(FontManager.getFont().deriveFont(40));

        selectWordFrame.add(selectWord, BorderLayout.NORTH);
        selectWordFrame.add(buttonPanel, BorderLayout.CENTER);

        word1.addActionListener(gameClient);

        Dimension d = new Dimension();
        d.height = 300;
        d.width = 100;

        buttonPanel.setPreferredSize(d);

        selectWordFrame.setSize(350, 120);
        selectWordFrame.setVisible(true);
        selectWordFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


    }

    public JFrame getSelectWordFrame() {
        return selectWordFrame;
    }

    public JButton getWord1() {
        return word1;
    }

    public JButton getWord2() {
        return word2;
    }
}
