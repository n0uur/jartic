package GameServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkListenerThread implements Runnable {

    private GameServerController gameServerController;

    private boolean isDestroyed;

    public void destroy() {
        this.isDestroyed = true;
    }

    @Override
    public void run() {
        while(!isDestroyed) {
            try {
                try (
                        ServerSocket gameServerSocket = new ServerSocket(20770);
                        Socket connectionSocket = gameServerSocket.accept();
                        BufferedReader clientPacket = new BufferedReader(
                                new InputStreamReader(connectionSocket.getInputStream())
                        );
                ) {
//                    gameServerController
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
