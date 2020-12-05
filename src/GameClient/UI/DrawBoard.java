//package GameClient.UI;
//
//import GameClient.Interactable;
//import GameClient.InteractionListener;
//
//import static com.raylib.Jaylib.*;
//
//
//public class DrawBoard implements Interactable, Drawable {
//    Rectangle drawBoard;
//    private InteractionListener interactionListener;
//
//    public DrawBoard() {
//        drawBoard = new Rectangle();
//        drawBoard.height((int) (660*0.68));
//        drawBoard.width(GetScreenWidth() - (int) (GetScreenWidth() * 0.25) - 30);
//        drawBoard.x((int) (GetScreenWidth() * 0.25) + 20);
//        drawBoard.y(60);
//    }
//
//    public void addInteractionListener(InteractionListener e) {
//        this.interactionListener = e;
//    }
//
//    public void drawGraphic() {
//        DrawRectangleLinesEx(drawBoard, 1, BLACK);
//    }
//}
