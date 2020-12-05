package Shared.Model;

public class GameServerStatus {
    public static enum gameStatus {
        GAME_WAITING, // รอผู้เล่นมา
        GAME_STARTING, // เกมกำลังเริ่ม
        GAME_STARTING_ROUND, // นำ player ที่กำลังอยู่ในเกมใส่ array เป็นคิวที่ต้องเล่น
        GAME_WAITING_WORD, // รอผู้เล่นที่เป็นตาวาดเลือกคำ
        GAME_PLAYING, // กำลังเล่น วาด หรือเดา
        GAME_NEXT_PLAYER, // กำลังรอเพื่อเปลี่ยนผู้เล่นเป็นคนต่อไป แสดงผลคำตอบ
        GAME_ENDED_ROUND, // ผูเล่นทุกคนในรอบ วาดหมดแล้ว เริ่มรอบต่อไปถ้าไม่มี คะแนน > 120 หรือจบเกม
        GAME_ENDED, // จบเกม เครียคะแนน ผู้เล่น ประกาศผลผู้ชนะ แล้วเตรียมเริ่มเกมใหม่
    }
}
