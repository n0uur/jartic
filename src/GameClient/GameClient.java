package GameClient;

import java.awt.*;
import java.awt.event.*;

public class GameClient implements MouseListener, MouseMotionListener, KeyListener{
    private boolean isClicked = false;
    private GameClientView gameClientView;
    private int mouseBtn, posXMouse, posYMouse;

    public int getMouseBtn() {
        return mouseBtn;
    }

    public void setMouseBtn(int mouseBtn) {
        this.mouseBtn = mouseBtn;
    }

    public int getPosXMouse() {
        return posXMouse;
    }

    public void setPosXMouse(int posXMouse) {
        this.posXMouse = posXMouse;
    }

    public int getPosYMouse() {
        return posYMouse;
    }

    public void setPosYMouse(int posYMouse) {
        this.posYMouse = posYMouse;
    }

    private int[][] drawPoints = new int[900][400];

    public GameClient() {
        this.gameClientView = new GameClientView(this);
    }

    public void chatMsgInputKeyPressed(java.awt.event.KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
            sendChatText();
    }
    public void sendChatText() {
        if(!this.gameClientView.getChatMsgInput().getText().isEmpty()) {
            String msgToSend = this.gameClientView.getChatMsgInput().getText() + '\n';
            this.gameClientView.getChatMsgInput().setText(this.gameClientView.getChatLogMsg().getText() + msgToSend);
            this.gameClientView.getChatMsgInput().setText(null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseBtn = e.getButton();
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

        int mousePosX = this.getPosXMouse();
        int mousePosY = this.getPosYMouse();
        int mouseBtn = this.getMouseBtn();

        if(mouseBtn == 1) {
            drawPoints[mousePosX][mousePosY] = 1;
        }
        else if(mouseBtn == 3) {
            drawPoints[mousePosX][mousePosY] = 0;
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
