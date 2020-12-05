package GameClient;

import java.awt.*;
import java.awt.event.*;

public class GameClientController implements MouseListener, MouseMotionListener, KeyListener{
    private boolean isClicked = false;
    private GameClientView gameClientView;
    private int mouseBtn, posXMouse, posYMouse;
    private int[][] drawPoints = new int[900][400];

    public GameClientController() {
        this.gameClientView = new GameClientView(this);
    }
    public void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            posXMouse = (int) this.gameClientView.getCanvas1().getMousePosition().getX();
            posYMouse = (int) this.gameClientView.getCanvas1().getMousePosition().getY();
            if(mouseBtn == 1) {
                g2d.setPaint(Color.black);
                g2d.fillOval(posXMouse, posYMouse, 5, 5);
                drawPoints[posXMouse][posYMouse] = 1;
            }
            else if(mouseBtn == 3) {
                g2d.setPaint(Color.white);
                g2d.fillOval(posXMouse, posYMouse, 20, 20);
                drawPoints[posXMouse][posYMouse] = 0;
            }
        }catch (NullPointerException e){}
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
        doDrawing(this.gameClientView.getCanvas1().getGraphics());
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
