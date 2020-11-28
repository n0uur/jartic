package Test;

import Shared.GamePacket.C2S_JoinGame;
import com.google.gson.Gson;

public class Test {
    public static void main(String[] args) {
        Gson gson = new Gson();

        C2S_JoinGame joinGame = new C2S_JoinGame();
        joinGame.playerName = "test";

        String s = gson.toJson(joinGame);

        System.out.println(s);

        C2S_JoinGame fromJsonObject = (C2S_JoinGame) gson.fromJson(s, C2S_JoinGame.class);

        System.out.println(fromJsonObject.playerName);
    }
}
