package GameClient.Model;

import Shared.Logger.GameLog;
import Shared.Model.PlayerProfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LocalPlayerData {
    private static String playerName = null;

    private static int id;

    private static String token;

    private static PlayerProfile playerProfile;

    private static final String saveFileName = "playerName.txt";

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        LocalPlayerData.id = id;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        LocalPlayerData.token = token;
    }

    public static PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public static void setPlayerProfile(PlayerProfile playerProfile) {
        LocalPlayerData.playerProfile = playerProfile;
    }

    public static String getPlayerName() {
        if (playerName == null) {
            loadPlayerName();
        }
        return playerName;
    }

    public static void setPlayerName(String name) {
        playerName = name;
        savePlayerName(name);
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

    public static void savePlayerName(String name) {
        File playerNameFile = new File(saveFileName);
        if(playerNameFile.exists()) {
            playerNameFile.delete();
            GameLog.Log("deleted old save..");
        }
        try(FileOutputStream fOut = new FileOutputStream(playerNameFile);) {
            for(int i = 0; i < name.length(); i++) {
                fOut.write(name.charAt(i));
            }
            GameLog.Log("saved new name successfully..");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
