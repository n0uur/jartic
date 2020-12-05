package GameClient;

import GameClient.UI.FontManager;
import GameClient.UI.PlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameClientRun extends javax.swing.JFrame implements MouseListener, Runnable, MouseMotionListener {

    public GameClientRun() {
        initComponents();
    }

    private void initComponents() {

        playerList = new javax.swing.JPanel();
        player = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        canvas1 = new java.awt.Canvas();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatLogMsg = new javax.swing.JTextArea();
        chatMsgInput = new javax.swing.JTextField();
        currentDrawing = new javax.swing.JLabel();
        thisPlayerName = "Test 1";

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
        for(int i = 1; i < 12; i++)
            playerList.add(new PlayerComponent("Tester " + i).getPlayerPanel());

        canvas1.setSize(900, 400);
        canvas1.setBackground(new java.awt.Color(255, 255, 255));
        canvas1.addMouseListener(this);
        canvas1.addMouseMotionListener(this);

        this.jPanel2.add(this.canvas1);

        chatLogMsg.setEditable(false);
        chatLogMsg.setColumns(35);
        chatLogMsg.setRows(5);
        chatLogMsg.setLineWrap(true);


        chatLogMsg.setFont(FontManager.getFont());
        chatLogMsg.setText("Hello\nสวัสดี\n");
        jScrollPane1.setViewportView(chatLogMsg);

        chatMsgInput.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        chatMsgInput.setFont(FontManager.getFont());
        chatMsgInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chatMsgInputKeyPressed(evt);
            }
        });

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
        drawingPlayer = "Tester 1";
        currentDrawing.setText(currentDrawing.getText() + drawingPlayer);

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
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseBtn = e.getButton();
        Thread canvasUpdate = new Thread(this);
//        canvasUpdate.start();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isClicked = !isClicked;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        canvas1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
//        canvas1.setCursor(getToolkit().createCustomCursor(getToolkit().getImage("resource/xxxx.png"), new Point(this.getX(), this.getY()), ""));
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void chatMsgInputKeyPressed(java.awt.event.KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
            sendChatText();
    }
    private void sendChatText() {
        if(!this.chatMsgInput.getText().isEmpty()) {
            String msgToSend = this.thisPlayerName + " : " + this.chatMsgInput.getText() + '\n';
            this.chatLogMsg.setText(this.chatLogMsg.getText() + msgToSend);
            this.chatMsgInput.setText(null);
        }
    }
    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            posXMouse = (int) this.canvas1.getMousePosition().getX();
            posYMouse = (int) this.canvas1.getMousePosition().getY();
            if(mouseBtn == 1) {
                g2d.setPaint(Color.black);
                g2d.fillOval(posXMouse, posYMouse, 5, 5);
                drawPoints[posXMouse][posYMouse] = 1;
            }
            else if(mouseBtn == 3) {
                g2d.setPaint(Color.white);
                g2d.fillOval(posXMouse, posYMouse, 15, 15);
                drawPoints[posXMouse][posYMouse] = 0;
            }
        }catch (NullPointerException e){}
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        doDrawing(canvas1.getGraphics());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            canvas1.repaint();
            for(int i = 0; i < 900; i++) {
                for(int j = 0; j < 400; j++) {
                    if(drawPoints[i][j] == 1) {
                        canvas1.getGraphics().fillOval(i, j, 5, 5);
                    }
                }
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GameClientRun().setVisible(true));
    }

    // Variables declaration - do not modify
    private java.awt.Canvas canvas1;
    private javax.swing.JTextArea chatLogMsg;
    private javax.swing.JTextField chatMsgInput;
    private javax.swing.JLabel currentDrawing;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel player;
    private javax.swing.JPanel playerList;
    private String drawingPlayer;
    private String thisPlayerName;
    private boolean isClicked = false;
    private int mouseBtn, posXMouse, posYMouse;
    private int[][] drawPoints = new int[900][400];

    // End of variables declaration
}
