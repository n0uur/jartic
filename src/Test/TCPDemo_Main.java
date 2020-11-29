package Test;

public class TCPDemo_Main {
    public static void main(String[] args) {

        new Thread(new TCPServer()).start();

        new Thread(new TCPClient1("A")).start();
        new Thread(new TCPClient1("B")).start();
        new Thread(new TCPClient1("C")).start();
    }
}
