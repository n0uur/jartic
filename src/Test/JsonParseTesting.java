package Test;

import Shared.GamePacket.C2S_JoinGame;
import com.google.gson.Gson;

import java.util.ArrayList;

public class JsonParseTesting {

    public static void main(String[] args) {
        Gson gson = new Gson();

        Game game = new Game();
        game.name = "The Sims";
        game.enn = Game.j.BBBB;
        game.intro = new ArrayList<Video>();
        game.intro.add(new Video("asdasd"));
        game.intro.add(new Video("fasf"));
        game.intro.add(new Video("a"));
        game.intro.add(new Video("asffge"));
        game.intro.add(new Video("asfawdasda"));

        String s = gson.toJson(game);

        System.out.println(s);

        Game reversed = (Game) gson.fromJson(s, Game.class);

        System.out.println(reversed.name);
        System.out.println(reversed.intro.get(1).name);
        System.out.println(reversed.enn == Game.j.BBBB);
        System.out.println(reversed.enn == Game.j.AAAA);
    }
}
