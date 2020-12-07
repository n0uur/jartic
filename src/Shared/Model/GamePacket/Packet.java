package Shared.Model.GamePacket;

public abstract class Packet {

    public static enum PacketID {
        C2S_ChatMessage,
        C2S_HeartBeat,
        C2S_JoinGame,
        C2S_RequestUpdatePlayers, // not using, server will auto sent an updates.
        C2S_RequestUpdateProfile, // not using, server will auto sent an updates.
        C2S_RequestUpdateWhiteBoard, // not using, server will auto sent an updates.
        C2S_SelectWord,
        C2S_UpdateWhiteBoard,
        C2S_GameClose,

        S2C_AcceptJoinGameRequest,
        S2C_ChatMessage,
        S2C_RequestHeartBeat,
        S2C_RequestWord,
        S2C_ServerHeartBeat,
        S2C_UpdateServerData,
        S2C_UpdatePlayers, // not using, we will use update server data instead
        S2C_UpdateProfile, // not using, we will use update server data instead
        S2C_UpdateWhiteBoard,
        S2C_ServerClose,
    }

    public PacketID PacketId = PacketID.valueOf(this.getClass().getSimpleName());
}
