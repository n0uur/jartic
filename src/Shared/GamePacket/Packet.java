package Shared.GamePacket;

public abstract class Packet {

    public static enum PacketID {
        C2S_ChatMessage,
        C2S_HeartBeat,
        C2S_JoinGame,
        C2S_RequestUpdatePlayers,
        C2S_RequestUpdateProfile,
        C2S_RequestUpdateWhiteBoard,
        C2S_SelectWord,
        C2S_UpdateWhiteBoard,

        S2C_AcceptJoinGameRequest,
        S2C_ChatMessage,
        S2C_RequestHeartBeat,
        S2C_RequestWord,
        S2C_ServerHeartBeat,
        S2C_UpdatePlayers,
        S2C_UpdateProfile,
        S2C_UpdateWhiteBoard,
    }

    public PacketID PacketId = PacketID.valueOf(this.getClass().getSimpleName());
}
