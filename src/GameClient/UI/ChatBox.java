package GameClient.UI;

import GameClient.Interactable;
import GameClient.InteractionListener;
import com.raylib.Raylib;

import static com.raylib.Jaylib.*;

public class ChatBox implements Interactable, Drawable, Runnable {
    Rectangle chatBox, textBox, textField;
    private InteractionListener interactionListener;
    Font font;
    String text;
    boolean isSelectTextField = false;

    private long lastClick = System.currentTimeMillis();

    public ChatBox() {
        font = new Font(LoadFont("resource/FC Iconic Regular.ttf"));
        chatBox = new Rectangle();
        chatBox.width(GetScreenWidth() - (int) (GetScreenWidth() * 0.25) - 30);
        chatBox.height((int) (660*0.28));
        chatBox.x((int) (GetScreenWidth() * 0.25) + 20);
        chatBox.y((int) (660*0.68) + 70);

        textBox = new Rectangle();
        textBox.width(chatBox.width() - 5);
        textBox.height(chatBox.height() - 5);
        textBox.x(chatBox.x() + 5);
        textBox.y(chatBox.y() + 5);

        textField = new Rectangle();
        textField.width(chatBox.width() - 10);
        textField.height((int) ((660*0.28)*0.2));
        textField.x(chatBox.x() + 5);
        textField.y(chatBox.y() + chatBox.height() - textField.height() - 5);

        Thread textFieldCheck = new Thread(this);
        textFieldCheck.start();
    }

    public void addInteractionListener(InteractionListener e) {
        this.interactionListener = e;
    }

    public void drawGraphic() {
        DrawRectangleLinesEx(chatBox, 1, BLACK);
    }

    public void drawText(String s) {
        text = s;
        DrawTextRec(font, text, textBox, 20, 1, false, BLACK);
    }

    public void drawTextField() {
        DrawRectangleLinesEx(textField, 1, BLACK);
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = System.currentTimeMillis();
            if (this.interactionListener != null) {
                if (CheckCollisionPointRec(GetMousePosition(), textField) && IsMouseButtonReleased(MOUSE_LEFT_BUTTON)) {
                    if(currentTime - lastClick > 50) {
                        System.out.println("Mouse Pressed");
                        lastClick = currentTime;
                    }
                }
            }
        }
    }
}
