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
