package GameClient;

import GameClient.Model.GameClientStatus;
import GameClient.Model.LocalPlayerData;
import GameClient.Network.ClientNetworkListener;
import GameClient.UI.SelectWord;
import Shared.Logger.GameLog;
import Shared.Model.DrawingBoard;
import Shared.Model.GamePacket.*;
import GameServer.Model.ServerPacket;
import Shared.Model.GameServerStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameClient implements MouseListener, MouseMotionListener, KeyListener, ActionListener, WindowListener{
    private boolean isClicked = false;
    private GameClientView gameClientView;
    private int mouseBtn, posXMouse, posYMouse;

    private ClientNetworkListener clientNetworkListener;
    private Thread networkListenerThread;

    private ClientPacketHandler clientPacketHandler;
    private Thread clientPacketHandlerThread;

    private ArrayList<ServerPacket> serverPackets;

    private GameClientStatus gameStatus;

    private long lastServerResponse;

    private GameServerStatus gameServerState;

    private boolean isDrawer;
    private String realWord;
    private String hintWord;
    private int timer;

    private boolean dragging = false;
    private Point last;
    private boolean isErasing;

    public boolean isErasing() {
        return isErasing;
    }

    public void setErasing(boolean erasing) {
        isErasing = erasing;
    }

    private SelectWord selectWord;

    private DrawingBoard drawingBoard;
    private long lastUpdateDrawingBoard;

    public GameClient() {

        this.drawingBoard = new DrawingBoard(928, 424);

        this.gameClientView = new GameClientView(this);

        this.clientNetworkListener = new ClientNetworkListener(this);
        this.clientPacketHandlerThread = new Thread(this.clientNetworkListener);
        this.clientPacketHandlerThread.start();

        this.clientPacketHandler = new ClientPacketHandler(this);
        this.clientPacketHandlerThread = new Thread(this.clientPacketHandler);
        this.clientPacketHandlerThread.start();

        this.serverPackets = new ArrayList<ServerPacket>();

        this.gameStatus = GameClientStatus.GAME_CONNECTING;

        C2S_JoinGame joinPacket = new C2S_JoinGame();
        joinPacket.playerName = LocalPlayerData.getPlayerName();
        joinPacket.sendToServer();

        long sendTime = System.currentTimeMillis();

        while(this.gameStatus == GameClientStatus.GAME_CONNECTING) {
            if(System.currentTimeMillis() - sendTime > 5000) {
                JOptionPane.showMessageDialog(null, "Cannot connect to server...", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.realWord = "";
        this.hintWord = "";
    }

    public void update() {
        // todo : game update by time..

        long currentTime = System.currentTimeMillis();

        if(currentTime - this.lastServerResponse > 15000) {
            // todo : game time out.. exit :(

            JOptionPane.showMessageDialog(null, "Lost connection with server :(", "Error", JOptionPane.ERROR_MESSAGE);
            this.gameStatus = GameClientStatus.GAME_NEED_TO_QUIT;
            return;
        }

        // todo : game logic by game state

        if(this.getGameServerState() == GameServerStatus.GAME_WAITING) {
            this.getGameClientView().getCurrentDrawing().setText("Waiting for more players..");
        }
        else if(this.getGameServerState() == GameServerStatus.GAME_WAITING_WORD) {
            this.getGameClientView().getCurrentDrawing().setText("Waiting drawer to selecting word... " + this.getTimer());
        }
        else if(this.getGameServerState() == GameServerStatus.GAME_PLAYING) {
            if(isDrawer)
                this.getGameClientView().getCurrentDrawing().setText("Word is : " + this.getRealWord() + "   ("+ this.getTimer() +" seconds left)");
            else
                this.getGameClientView().getCurrentDrawing().setText("Hint : " + this.getHintWord() + "   ("+ this.getTimer() +" seconds left)");
        }
        else if(this.getGameServerState() == GameServerStatus.GAME_NEXT_PLAYER) {
            this.getGameClientView().getCurrentDrawing().setText("Selecting next player");
        }
        else if(this.getGameServerState() == GameServerStatus.GAME_ENDED_ROUND) {
            this.getGameClientView().getCurrentDrawing().setText("Round ended! ...starting in "+ this.getTimer() +" seconds.");
        }
    }

    public GameClientStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameClientStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getMouseBtn() {
        return mouseBtn;
    }

    public void setMouseBtn(int mouseBtn) {
        this.mouseBtn = mouseBtn;
    }

    public void chatMsgInputKeyPressed(java.awt.event.KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
            sendChatMessage();
    }

    public GameClientView getGameClientView() {
        return gameClientView;
    }

    public void sendChatMessage() {
        if(!this.gameClientView.getChatMsgInput().getText().isEmpty()) {
            String msgToSend = this.gameClientView.getChatMsgInput().getText();
            this.gameClientView.getChatMsgInput().setText(this.gameClientView.getChatLogMsg().getText() + msgToSend);
            this.gameClientView.getChatMsgInput().setText(null);
            C2S_ChatMessage chatMessage = new C2S_ChatMessage();
            chatMessage.message = msgToSend;
            chatMessage.sendToServer();
        }
    }

    public ArrayList<ServerPacket> getServerPackets() {
        return serverPackets;
    }

    public synchronized void addServerPacket(ServerPacket serverPacket) {
        this.serverPackets.add(serverPacket);
    }

    public long getLastServerResponse() {
        return this.lastServerResponse;
    }

    public void serverResponse() {
        this.lastServerResponse = System.currentTimeMillis();
    }

    public boolean isDrawer() {
        return isDrawer;
    }

    public void isDrawer(boolean drawing) {
        isDrawer = drawing;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public String getRealWord() {
        return realWord;
    }

    public void setRealWord(String realWord) {
        this.realWord = realWord;
    }

    public String getHintWord() {
        return hintWord;
    }

    public void setHintWord(String hintWord) {
        this.hintWord = hintWord;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        last = e.getPoint();
        dragging = true;
        this.getGameClientView().getCanvasPanel().repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        last = null;
        dragging = false;
        setErasing(false);
        this.getGameClientView().getCanvasPanel().repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.gameClientView.getCanvas1().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    public int[][] getPoints() {
        return this.drawingBoard.getDrawingPoints();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            if(dragging && isDrawer()) {
                if(e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
                    setErasing(false);
                    this.drawingBoard.getDrawingPoints()[e.getX()][e.getY()] = 1;
                }
                else {
                    setErasing(true);
                    for(int i = Math.max(e.getX() - 15, 0); i < Math.min(e.getX() + 15, 928); i++) {
                        for(int j = Math.max(e.getY() - 15, 0); j < Math.min(e.getY() + 15, 424); j++) {
                            this.drawingBoard.getDrawingPoints()[i][j] = 0;
                        }
                    }
                }

                if(System.currentTimeMillis() - lastUpdateDrawingBoard > 500) {
                    lastUpdateDrawingBoard = System.currentTimeMillis();

                    C2S_UpdateWhiteBoard whiteBoardPacket = new C2S_UpdateWhiteBoard();
                    whiteBoardPacket.currentBoard = this.drawingBoard.toString();
                    whiteBoardPacket.sendToServer();

                }
            }
        }catch (ArrayIndexOutOfBoundsException Ignored){}
        this.getGameClientView().getCanvasPanel().repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        chatMsgInputKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public GameServerStatus getGameServerState() {
        return gameServerState;
    }

    public void setGameServerState(GameServerStatus gameServerState) {
        this.gameServerState = gameServerState;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(selectWord.getWord1()) || e.getSource().equals(selectWord.getWord2())) {
            String selectedWord = ((JButton) e.getSource()).getText();
            C2S_SelectWord selectedPacket = new C2S_SelectWord();
            selectedPacket.word = selectedWord;
            selectedPacket.sendToServer();
            selectWord.getSelectWordFrame().dispose();
        }
    }

    public void newSelectWord(String word1, String word2) {
        selectWord = new SelectWord( this ,word1, word2);
    }

    public void destroySelectWord() {
        try {
            selectWord.getSelectWordFrame().dispose();
        }catch (NullPointerException Ignored){}
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        C2S_GameClose closePacket = new C2S_GameClose();
        closePacket.sendToServer();
        GameLog.warn("Closing...");
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public DrawingBoard getDrawingBoard() {
        return drawingBoard;
    }


}
