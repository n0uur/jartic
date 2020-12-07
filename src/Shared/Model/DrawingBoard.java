package Shared.Model;

import Shared.Logger.GameLog;
import Shared.RunLength;

public class DrawingBoard {
    private int[][] drawingPoints;
    private int sizeX, sizeY;

    public DrawingBoard(int x, int y) {
        sizeX = x;
        sizeY = y;
        this.drawingPoints = new int[x][y];
    }

    public int[][] getDrawingPoints() {
        return this.drawingPoints;
    }

    public void update(int x, int y, int value) {
        try {
            if (value == 1) {
                drawingPoints[x][y] = 1;
            } else if (value == 0) {
                for (int i = Math.max(x - 15, 0); i < Math.min(x + 15, 928); i++) {
                    for (int j = Math.max(y - 15, 0); j < Math.min(y + 15, 424); j++) {
                        drawingPoints[i][j] = 0;
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored) { }
    }

    public void clear() {
        for(int i = 0; i < sizeX; i++) {
            for(int j = 0; j < sizeY; j++) {
                drawingPoints[i][j] = 0;
            }
        }
    }

}
