package Shared.Model;

public class PlayerProfile {

    private String name;
    private int score;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerProfile() {
        this("");
    }

    public PlayerProfile(String name) {
        this.setName(name);
        this.setScore(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void resetScore() {
        this.score = 0;
    }
}
