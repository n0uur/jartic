package GameClient.UI;

import GameClient.UI.Drawable;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import static com.raylib.Jaylib.*;

public class Button implements Drawable {
    Rectangle rect;
    String txt;
    int width, x, y;
    Raylib.Vector2 txtSize;
    public Button(String txt, int width, int x, int y) {
        this.width = width;
        this.txt = txt;
        this.x = x;
        this.y = y;
        rect = new Rectangle();
        rect.height(50);
        rect.width(width);
        rect.x(x);
        rect.y(y);
        txtSize = MeasureTextEx(LoadFont("src/Test/supermarket.ttf"), txt, 32, 1);
    }

    public void drawGraphic() {
        DrawRectangleRec(rect, VIOLET);
        DrawText(txt, x + ((int) txtSize.x()/2) - width + (int) txtSize.x(), y + (int) txtSize.y()/2, 32, BLACK);
    }
}
