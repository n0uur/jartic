package GameClient.LocalSave;

import Shared.Logger.GameLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LocalPlayerName {
    private static String playerName = null;

    private static final String saveFileName = "playerName.txt";

    public static String getPlayerName() {
        if (playerName == null) {
            loadPlayerName();
        }
        return playerName;
    }

    public static void setPlayerName(String name) {
        playerName = name;
        savePlayerName();
    }

    public static void loadPlayerName() {
        File playerNameFile = new File(saveFileName);
        if(playerNameFile.exists()) {
            playerName = "";
            try (FileInputStream fIn = new FileInputStream(playerNameFile)) {
                int i;
                while ((i = fIn.read()) != -1) {
                    playerName += (char) i;
                }
                GameLog.Log("loaded name from save..");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void savePlayerName() {
        File playerNameFile = new File(saveFileName);
        if(playerNameFile.exists()) {
            playerNameFile.delete();
            GameLog.Log("deleted old save..");
        }
        try(FileOutputStream fOut = new FileOutputStream(playerNameFile);) {
            for(int i = 0; i < playerName.length(); i++) {
                fOut.write(playerName.charAt(i));
            }
            GameLog.Log("saved new name successfully..");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}