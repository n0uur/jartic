package GameClient;

import GameClient.Model.GameClientStatus;
import GameClient.Model.LocalPlayerData;
import GameClient.Network.ClientNetworkListener;
import Shared.Model.GamePacket.C2S_JoinGame;
import GameServer.Model.ServerPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameClient implements MouseListener, MouseMotionListener, KeyListener{
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

    private int[][] drawPoints;

    public GameClient() {

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

        this.gameClientView = new GameClientView(this);
        this.drawPoints = new int[900][400];
    }

    public void update() {
        // todo : game update by time..

        long currentTime = System.currentTimeMillis();

        if(currentTime - this.lastServerResponse > 10000) {
            // todo : game time out.. exit :(

            JOptionPane.showMessageDialog(null, "Lost connection with server :(", "Error", JOptionPane.ERROR_MESSAGE);
            this.gameStatus = GameClientStatus.GAME_NEED_TO_QUIT;
            return;
        }

        // todo : game logic by game state
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
    public void sendChatMessage() {
        if(!this.gameClientView.getChatMsgInput().getText().isEmpty()) {
            String msgToSend = this.gameClientView.getChatMsgInput().getText() + '\n';
            this.gameClientView.getChatMsgInput().setText(this.gameClientView.getChatLogMsg().getText() + msgToSend);
            this.gameClientView.getChatMsgInput().setText(null);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.setMouseBtn(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.isClicked = !this.isClicked;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.gameClientView.getCanvas1().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        int mousePosX = e.getX();
        int mousePosY = e.getY();
        int mouseBtn = this.getMouseBtn();

        if(this.getMouseBtn() == 1) {
            try {
                drawPoints[mousePosX][mousePosY] = 1;
            }catch (ArrayIndexOutOfBoundsException Ignored){}
        }
        else if(this.getMouseBtn() == 3) {
            try {
                drawPoints[mousePosX][mousePosY] = 0;
            }catch (ArrayIndexOutOfBoundsException Ignored){}
        }

        this.gameClientView.doDrawing(mousePosX, mousePosY, mouseBtn);
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
}
