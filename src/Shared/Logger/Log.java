package Shared.Logger;

public abstract class Log {
    public static void Log(String s) {
        System.out.println("[Log] " + s);
    };

    public static void Error(String s) {
        System.out.print("\u001B[31m");
        Log(s);
        System.out.print("\u001B[0m");
    }

    public static void Warn(String s) {
        System.out.print("\u001B[33m");
        Log(s);
        System.out.print("\u001B[0m");
    }
}
