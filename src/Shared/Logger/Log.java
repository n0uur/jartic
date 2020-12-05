package Shared.Logger;

public abstract class Log {
    public static void log(String s) {
        System.out.println("[Log] " + s);
    };

    public static void error(String s) {
        System.out.print("\u001B[31m");
        log(s);
        System.out.print("\u001B[0m");
    }

    public static void warn(String s) {
        System.out.print("\u001B[33m");
        log(s);
        System.out.print("\u001B[0m");
    }
}
