package Test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

class DragCircle extends JPanel {

    private final class MouseDrag extends MouseAdapter {
        private boolean dragging = false;
        private Point last;

        @Override
        public void mousePressed(MouseEvent m) {
            last = m.getPoint();
            dragging = isInsideEllipse(last);
            if (!dragging) {
                x = last.x;
                y = last.y;
                width = 0;
                height = 0;
            }
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent m) {
            last = null;
            dragging = false;
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent m) {
            int dx = m.getX() - last.x;
            int dy = m.getY() - last.y;
            if (dragging) {
                x += dx;
                y += dy;
            } else {
                width += dx;
                height += dy;
            }
            last = m.getPoint();
            repaint();
        }
    }

    private int x;
    private int y;
    private int width;
    private int height;

    private MouseDrag mouseDrag;

    public DragCircle() {
        setBackground(Color.WHITE);
        mouseDrag = new MouseDrag();
        addMouseListener(mouseDrag);
        addMouseMotionListener(mouseDrag);
    }

    public boolean isInsideEllipse(Point point) {
        return new Ellipse2D.Float(x, y, width, height).contains(point);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(x, y, width, height);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(300, 300);
//        jFrame.add(new DragCircle());
        JTextPane pane = new JTextPane();
        jFrame.add(pane);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for(int i = 0; i < 100; i++) {
            DragCircle.appendToPane(pane, i + "\n", Color.ORANGE);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
}