package GameClient;

import GameClient.UI.FontManager;
import GameClient.UI.PlayerComponent;

import javax.swing.*;
import java.awt.*;

public class GameClientView extends javax.swing.JFrame {
    private java.awt.Canvas canvas1;
    private javax.swing.JTextArea chatLogMsg;
    private javax.swing.JTextField chatMsgInput;
    private javax.swing.JLabel currentDrawing;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel player;
    private javax.swing.JPanel playerList;
    private GameClient gameClient;

    public Canvas getCanvas1() {
        return canvas1;
    }

    public JTextField getChatMsgInput() {
        return chatMsgInput;
    }

    public JTextArea getChatLogMsg() {
        return chatLogMsg;
    }

    public GameClientView(GameClient gameClient) {
        this.gameClient = gameClient;
        playerList = new javax.swing.JPanel();
        player = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        canvas1 = new java.awt.Canvas();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatLogMsg = new javax.swing.JTextArea();
        chatMsgInput = new javax.swing.JTextField();
        currentDrawing = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jartic");
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1280, 768));
        setResizable(false);

        playerList.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        playerList.setPreferredSize(new java.awt.Dimension(320, 60));
        playerList.setLayout(new java.awt.GridLayout(12, 0));

        player.setFont(FontManager.getFont().deriveFont(40f)); // NOI18N
        player.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player.setText("PlayerComponent");

        playerList.add(player);
        for (int i = 1; i < 12; i++)
            playerList.add(new PlayerComponent("Tester " + i).getPlayerPanel());

        canvas1.setSize(900, 400);
        canvas1.setBackground(new java.awt.Color(255, 255, 255));

        this.jPanel2.add(this.canvas1);
        canvas1.addMouseListener(gameClient);
        canvas1.addMouseMotionListener(gameClient);

        chatLogMsg.setEditable(false);
        chatLogMsg.setColumns(35);
        chatLogMsg.setRows(5);
        chatLogMsg.setLineWrap(true);


        chatLogMsg.setFont(FontManager.getFont());
        jScrollPane1.setViewportView(chatLogMsg);

        chatMsgInput.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        chatMsgInput.setFont(FontManager.getFont());
        chatMsgInput.addKeyListener(gameClient);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(chatMsgInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(chatMsgInput, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(19, Short.MAX_VALUE))
        );

        currentDrawing.setFont(FontManager.getFont().deriveFont(36f)); // NOI18N
        currentDrawing.setText("Current Drawing : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(playerList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addComponent(currentDrawing, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(currentDrawing, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(playerList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
        setVisible(true);
    }

    public void doDrawing(int mousePosX, int mousePosY, int mouseBtn) {
        Graphics2D g2d = (Graphics2D) this.getCanvas1().getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            if(mouseBtn == 1) {
                g2d.setPaint(Color.black);
                g2d.fillOval(mousePosX, mousePosY, 5, 5);

            }
            else if(mouseBtn == 3) {
                g2d.setPaint(Color.white);
                g2d.fillOval(mousePosX, mousePosY, 20, 20);
            }
        }catch (NullPointerException e){}
    }

}