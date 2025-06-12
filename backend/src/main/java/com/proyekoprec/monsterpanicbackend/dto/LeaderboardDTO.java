package com.proyekoprec.monsterpanicbackend.dto;

public class LeaderboardDTO {
    private String name;
    private int score;
    private String mode;

    public LeaderboardDTO(String name, int score, String mode) {
        this.name = name;
        this.score = score;
        this.mode = mode;
    }
    
    // Getter dan Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
}