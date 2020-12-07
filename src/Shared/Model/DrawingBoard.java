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

    public void fromString(String s) {
        String rawString = RunLength.decode(s);
        for(int i = 0; i < s.length(); i++) {
            drawingPoints[i % sizeX][Math.floorDiv(i, sizeX)] = rawString.charAt(i);
        }
    }

    public int[][] getDrawingPoints() {
        return this.drawingPoints;
    }

    public String toString() {
        GameLog.log("converting to string...");
        String result = "";
        for(int i = 0; i < sizeX; i++) {
            for(int j = 0; j < sizeY; j++) {
                result += drawingPoints[i][j] + "";
            }
        }
        GameLog.log("converted...");
        return RunLength.encode(result);
    }

    public void clear() {
        for(int i = 0; i < sizeX; i++) {
            for(int j = 0; j < sizeY; j++) {
                drawingPoints[i][j] = 0;
            }
        }
    }

}
