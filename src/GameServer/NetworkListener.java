package GameServer;

import Shared.GamePacket.ClientPacket;
import Shared.Logger.ServerLog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import netscape.javascript.JSObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkListener implements Runnable {

    private GameServerController gameServerController;

    private boolean isDestroyed;

    public NetworkListener(GameServerController gameServerController) {
        this.gameServerController = gameServerController;
    }

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
                    String jsonString = clientPacket.readLine();
                    JsonObject rawObject = JsonParser.parseString(jsonString).getAsJsonObject();
                    String packetName = rawObject.get("packetName").getAsString();

                    ClientPacket realPacket = null;

                    if(packetName.equals("C2S_JoinGame")) {

                    }

                    if(realPacket != null)
                        this.gameServerController.addNetworkIncomePacket(realPacket);
                    else {
                        ServerLog.Error("Dropped packet! : ");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
