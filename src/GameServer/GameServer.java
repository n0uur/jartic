package GameServer;

import GameServer.Model.ServerPlayer;
import GameServer.Model.WordList;
import GameServer.Network.ServerNetworkListener;
import GameClient.Model.ClientPacket;
import Shared.Logger.ServerLog;
import Shared.Model.GamePacket.*;
import Shared.Model.GameServerStatus;
import Shared.Model.PlayerProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameServer {

    // server worker

    private ServerNetworkListener serverNetworkListener;
    private Thread networkListenerThread;

    private ServerPacketHandler serverPacketHandler;
    private Thread packetHandlerThead;

    private ArrayList<ClientPacket> packets;

    // game object

    // private ArrayList<Player> players;
    private GameServerStatus currentGameStatus;

    private long lastBroadcastDataUpdate;

    private ArrayList<ServerPlayer> playerDrawingQueue;
    private ServerPlayer drawingPlayer;
    private String drawingWord;

    private long startPlayingTime;

    private boolean isGameEndedBroadcastScore;
    private long gameEndedBroadcastScoreTime;

    private boolean isStartWaitingWord;
    private long startWaitingWordTime;

    private int answerScore;

    private long lastBroadcastWhiteboard;

    //

    public GameServer() {
        ServerLog.log("Initializing Server..");

        this.packets = new ArrayList<ClientPacket>();

        ServerLog.log("Creating Server worker & listener..");

        this.serverNetworkListener = new ServerNetworkListener(this);
        this.networkListenerThread = new Thread(this.serverNetworkListener);
        this.networkListenerThread.start();

        this.serverPacketHandler = new ServerPacketHandler(this);
        this.packetHandlerThead = new Thread(this.serverPacketHandler);
        this.packetHandlerThead.start();

        ServerLog.log("Creating game object..");

        WordList.loadWordList();

        ServerLog.log("Server start successfully..");
    }

    public void update() {

        long currentTime = System.currentTimeMillis();

        // check if player not enough then reset game to waiting status !
        if(ServerPlayer.getPlayers().size() < 2 && this.getCurrentGameStatus() != GameServerStatus.GAME_WAITING) {

            this.setCurrentGameStatus(GameServerStatus.GAME_WAITING); // instant end game if player not enough.

        }

        // check player connection is still alive
        synchronized (this) {
            for(int i = 0; i < ServerPlayer.getPlayers().size(); i++) {

                ServerPlayer player = ServerPlayer.getPlayers().get(i);

                if(currentTime - player.getLastResponse() > 10000 && !player.isRequestedHeartBeat()) {

                    ServerLog.log(player.getPlayerProfile().getName() + " has no responses in 10 seconds..");

                    S2C_RequestHeartBeat requestPacket = new S2C_RequestHeartBeat();
                    requestPacket.sendToClient(player.getPeerId());

                    player.isRequestedHeartBeat(true);
                }

                if(currentTime - player.getLastResponse() > 18000 && player.isRequestedHeartBeat()) {

                    player.remove();

                    S2C_ChatMessage leftMessage = new S2C_ChatMessage();
                    leftMessage.flag = S2C_ChatMessage.messageFlag.MESSAGE_DANGER;
                    leftMessage.message = player.getPlayerProfile().getName() + " has lost his/her connection with our game :(";

                    ServerLog.log(player.getPlayerProfile().getName() + " has lost his/her connection.");

                    leftMessage.broadcastToClient();

                    this.broadcastGameDataUpdate();

                }
            }
        }

        // broadcast game data update if it's too long
        if(currentTime - this.lastBroadcastDataUpdate > 1000) {
            this.broadcastGameDataUpdate();
        }

        // todo :
        //  game logic up to game state..

        if(this.getCurrentGameStatus() == GameServerStatus.GAME_WAITING) {
            if(ServerPlayer.getPlayers().size() >= 2) {
                ServerLog.log("Game is starting with " + ServerPlayer.getPlayers().size() + "players.");
                this.setCurrentGameStatus(GameServerStatus.GAME_STARTING);
            }
        }

        else if(this.getCurrentGameStatus() == GameServerStatus.GAME_STARTING) {

            ServerLog.log("Game starting round.");

            this.setCurrentGameStatus(GameServerStatus.GAME_STARTING_ROUND);
        }

        else if(this.getCurrentGameStatus() == GameServerStatus.GAME_STARTING_ROUND) {

            this.playerDrawingQueue = new ArrayList<>(ServerPlayer.getPlayers());
            Collections.shuffle(this.playerDrawingQueue, new Random());

            ServerLog.log("Randomized drawing queue!");

            String queue = "";

            for(ServerPlayer player: this.playerDrawingQueue) {
                queue += " " + player.getPlayerProfile().getName();
            }

            S2C_ChatMessage chatPacket = new S2C_ChatMessage();
            chatPacket.message = "[Server] Drawing queue" + queue;
            chatPacket.broadcastToClient();

            this.setCurrentGameStatus(GameServerStatus.GAME_NEXT_PLAYER);

        }

        else if(this.getCurrentGameStatus() == GameServerStatus.GAME_WAITING_WORD) {
            if(!isStartWaitingWord) {
                isStartWaitingWord = true;
                startWaitingWordTime = System.currentTimeMillis();

                ServerLog.log("Waiting "+ drawingPlayer.getPlayerProfile().getName() +" to selecting word...");
            }

            if(isStartWaitingWord && currentTime - startWaitingWordTime > 10000) {
                ServerLog.warn(drawingPlayer.getPlayerProfile().getName() + " has lost his/her turn..");
                this.setCurrentGameStatus(GameServerStatus.GAME_NEXT_PLAYER);
                isStartWaitingWord = false;

                S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                chatPacket.message = "[Server] " + this.drawingPlayer.getPlayerProfile().getName() + " has lost his turn...";
                chatPacket.broadcastToClient();
            }
        }

        else if(this.getCurrentGameStatus() == GameServerStatus.GAME_PLAYING) {

            if(currentTime - this.startPlayingTime > 90000) { // more than 30 seconds stop it!
                this.setCurrentGameStatus(GameServerStatus.GAME_NEXT_PLAYER);
            }


            boolean allCorrected = true;
            for(int i = 0; i < ServerPlayer.getPlayers().size(); i++) {
                if(!ServerPlayer.getPlayers().get(i).getPlayerProfile().isDrawing() && !ServerPlayer.getPlayers().get(i).getPlayerProfile().isCorrected()) {
                    allCorrected = false;
                    break;
                }
            }
            if(allCorrected) {
                this.setCurrentGameStatus(GameServerStatus.GAME_NEXT_PLAYER);
            }
        }

        else if(this.getCurrentGameStatus() == GameServerStatus.GAME_NEXT_PLAYER) {

            ServerPlayer.getPlayers().forEach((player) -> {
                player.getPlayerProfile().setCorrected(false);
            });

            if(this.playerDrawingQueue.size() > 0) { // still have other in queue
                this.drawingPlayer = this.playerDrawingQueue.remove(0);

                ServerLog.log(drawingPlayer.getPlayerProfile().getName() + " is current drawer!");

                for (ServerPlayer player : ServerPlayer.getPlayers()) {
                    player.getPlayerProfile().isDrawing(this.drawingPlayer.getPeerId() == player.getPeerId());
                }

                S2C_RequestWord requestWord = new S2C_RequestWord();
                requestWord.words = WordList.getRandomWord(3);
                requestWord.sendToClient(this.drawingPlayer.getPeerId());

                ServerLog.log("requested words.");

                setAnswerScore(10);

                S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                chatPacket.message = "[Server] " + this.drawingPlayer.getPlayerProfile().getName() + " is selecting words...";
                chatPacket.broadcastToClient();

                // clear canvas
                S2C_UpdateWhiteBoard whiteBoardPacket = new S2C_UpdateWhiteBoard();
                whiteBoardPacket.needToClear = true;
                whiteBoardPacket.broadcastToClient();

                this.setCurrentGameStatus(GameServerStatus.GAME_WAITING_WORD);
            }
            else if(this.playerDrawingQueue.size() <= 0) {
                this.setCurrentGameStatus(GameServerStatus.GAME_ENDED_ROUND);
            }
        }

        else if(this.getCurrentGameStatus() == GameServerStatus.GAME_ENDED_ROUND) {

            ServerPlayer maxScorePlayer = ServerPlayer.getPlayers().get(0);

            for(int i = 1; i < ServerPlayer.getPlayers().size(); i++) {
                if(ServerPlayer.getPlayers().get(i).getPlayerProfile().getScore() > maxScorePlayer.getPlayerProfile().getScore()) {
                    maxScorePlayer = ServerPlayer.getPlayers().get(i);
                    break;
                }
            }

            if(maxScorePlayer.getPlayerProfile().getScore() >= 120) {

                S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                chatPacket.message = "[Server] Round ended! THE WINNER IS " + maxScorePlayer.getPlayerProfile().getName() + " !!";
                chatPacket.broadcastToClient();

                this.setCurrentGameStatus(GameServerStatus.GAME_ENDED);
            }
            else {

                S2C_ChatMessage chatPacket = new S2C_ChatMessage();
                chatPacket.message = "[Server] Round ended! Still no winner let's start new round!";
                chatPacket.broadcastToClient();

                this.setCurrentGameStatus(GameServerStatus.GAME_STARTING_ROUND);
            }

        }

        else if(this.getCurrentGameStatus() == GameServerStatus.GAME_ENDED) {

            if (!isGameEndedBroadcastScore)
            {
                // todo : broadcast who is the winner..

                for (int i = 0; i < ServerPlayer.getPlayers().size(); i++) {
                    ServerPlayer.getPlayers().get(i).getPlayerProfile().resetScore();
                }

                isGameEndedBroadcastScore = true;
                gameEndedBroadcastScoreTime = System.currentTimeMillis();
            }

            else if(isGameEndedBroadcastScore && currentTime - gameEndedBroadcastScoreTime > 10000) {

                isGameEndedBroadcastScore = false;

                this.setCurrentGameStatus(GameServerStatus.GAME_WAITING);

            }


        }

    }

    public void destroy() {
        ServerLog.log("Server is shutting down..");

        this.serverNetworkListener.destroy();
        this.serverPacketHandler.destroy();

        // todo : tell all player server is closing..

        ServerLog.log("Bye!");

    }

    public int getDrawingTimeLeft() {
        return (int) ((this.startPlayingTime + 90000 - System.currentTimeMillis()) / 1000);
    }

    public int getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(int answerScore) {
        this.answerScore = answerScore;
    }

    public boolean isStartWaitingWord() {
        return isStartWaitingWord;
    }

    public void setStartWaitingWord(boolean startWaitingWord) {
        isStartWaitingWord = startWaitingWord;
    }

    public String getDrawingWord() {
        return drawingWord;
    }

    public void setDrawingWord(String drawingWord) {
        this.drawingWord = drawingWord;
    }

    public synchronized void addNetworkIncomePacket(ClientPacket clientPacket) {
        ServerLog.log("Incoming packet ["+ clientPacket.PacketId +"]");
        this.packets.add(clientPacket);
    }

    public synchronized ClientPacket getQueuePacket() {
        return this.packets.remove(0);
    }

    public ArrayList<ClientPacket> getPackets() {
        return this.packets;
    }

    public GameServerStatus getCurrentGameStatus() {
        return currentGameStatus;
    }

    public void setCurrentGameStatus(GameServerStatus currentGameStatus) {

        ServerLog.log("Server changed state to " + currentGameStatus.toString() );

        if(currentGameStatus == GameServerStatus.GAME_PLAYING) {
            this.startPlayingTime = System.currentTimeMillis();
        }

        this.currentGameStatus = currentGameStatus;
        this.broadcastGameDataUpdate();
    }

    public void broadcastGameDataUpdate() {
        // todo : broadcast player current game state, update game, game data

        int timeLeft = 0;
        ArrayList<PlayerProfile> profiles = new ArrayList<PlayerProfile>();
        String realWord = "";
        String hintWord = "";

        long currentTime = System.currentTimeMillis();

        if(getCurrentGameStatus() == GameServerStatus.GAME_WAITING_WORD) {
            timeLeft = (int) ((this.startWaitingWordTime + 10000 - currentTime) / 1000);
        }
        else if(getCurrentGameStatus() == GameServerStatus.GAME_ENDED) {
            timeLeft = (int) ((this.gameEndedBroadcastScoreTime + 10000 - currentTime) / 1000);
        }
        else if(getCurrentGameStatus() == GameServerStatus.GAME_PLAYING) {
            timeLeft = (int) ((this.startPlayingTime + 90000 - currentTime) / 1000);
        }
        else if(getCurrentGameStatus() == GameServerStatus.GAME_STARTING) {
            // todo : add interval for starting game and calculate time left...
        }

        if(this.getCurrentGameStatus() == GameServerStatus.GAME_PLAYING) {
            realWord = this.getDrawingWord();
            for(int i = 0; i < realWord.length(); i++) {
                hintWord += (realWord.charAt(i) != (char) ' ' ? "_" : ' ');
            }
        }

        for (ServerPlayer player:
             ServerPlayer.getPlayers()) {
            profiles.add(player.getPlayerProfile());
        }

        S2C_UpdateServerData updatePacket = new S2C_UpdateServerData();
        updatePacket.gameServerStatus = this.getCurrentGameStatus();
        updatePacket.timeLeftInSeconds = timeLeft;
        updatePacket.playersProfile = profiles;
        updatePacket.hintWord = hintWord;
        updatePacket.realWord = realWord;
        if(drawingPlayer != null)
            updatePacket.drawerId = drawingPlayer.getPlayerProfile().getId();

        updatePacket.broadcastToClient();

        this.lastBroadcastDataUpdate = System.currentTimeMillis();

    }

    public ServerPlayer getDrawingPlayer() {
        return drawingPlayer;
    }

    public void setDrawingPlayer(ServerPlayer drawingPlayer) {
        this.drawingPlayer = drawingPlayer;
    }

    public long getLastBroadcastWhiteboard() {
        return lastBroadcastWhiteboard;
    }

    public void setLastBroadcastWhiteboard(long lastBroadcastWhiteboard) {
        this.lastBroadcastWhiteboard = lastBroadcastWhiteboard;
    }
}
