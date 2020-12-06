package Shared.Logger;

public class ServerLog extends Log {
    public static void log(String s) {
        System.out.println("[Server] " + s);
    }
}
