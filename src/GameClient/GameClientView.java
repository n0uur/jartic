package GameClient;

import GameClient.UI.ChatBox;
import GameClient.UI.DrawBoard;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import static com.raylib.Jaylib.*;

public class GameClientView {
    Rectangle allPlayerFrame;
    DrawBoard drawBoard;
    ChatBox chatBox;
    Font font;
    Jaylib.Vector2 currentDrawingPositionText, playerPositionText;

    public GameClientView() {
        InitWindow(1280, 720, "Jartic");
        font = new Font(LoadFont("resource/FC Iconic Regular.ttf"));

        drawBoard = new DrawBoard();
        chatBox = new ChatBox();

        allPlayerFrame = new Rectangle();
        allPlayerFrame.width((int) (GetScreenWidth() * 0.25));
        allPlayerFrame.height(GetScreenHeight() - 20);
        allPlayerFrame.x(10);
        allPlayerFrame.y(10);

        currentDrawingPositionText = new Jaylib.Vector2();
        currentDrawingPositionText.x((int) (GetScreenWidth() * 0.25) + 20);
        currentDrawingPositionText.y(10);

        playerPositionText = new Jaylib.Vector2();
        playerPositionText.x(15 + ((int) (GetScreenWidth() * 0.25) - 10 - MeasureTextEx(font, "PLAYER",40, 1).x())/2);
        playerPositionText.y(25);
        while(!WindowShouldClose()) {

            BeginDrawing();
            ClearBackground(RAYWHITE);
            DrawRectangleRoundedLines(allPlayerFrame, (float) 0.05, 0, 2, BLACK);
            DrawTextEx(font, "Current Drawing : ", currentDrawingPositionText, 40, 1, BLACK);


            //player
            DrawRectangleLines(15, 15, (int) (GetScreenWidth() * 0.25) - 10, 67, BLACK);
            DrawTextEx(font, "PLAYER", playerPositionText, 40, 1, BLACK);

            drawBoard.drawGraphic();

            //player frame
//            for (int i = 0; i<9;i++)
//                DrawRectangleLines(15, 84 + (69*i), (int) (GetScreenWidth() * 0.25) - 10, 67, BLACK);


            chatBox.drawGraphic();
            chatBox.drawText("test\nsdfsdfsdfs\nasadasdas\nasdasda\nasdasd");
            chatBox.drawTextField();
            EndDrawing();
        }

        Raylib.CloseWindow();
    }

}
