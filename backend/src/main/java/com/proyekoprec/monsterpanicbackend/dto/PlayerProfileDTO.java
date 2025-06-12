package com.proyekoprec.monsterpanicbackend.dto;

import java.util.List;

public class PlayerProfileDTO {
    private long totalGames;
    private int highestScore;
    private List<AchievementDTO> achievements;

    // Getter & Setter
    public long getTotalGames() { return totalGames; }
    public void setTotalGames(long totalGames) { this.totalGames = totalGames; }
    public int getHighestScore() { return highestScore; }
    public void setHighestScore(int highestScore) { this.highestScore = highestScore; }
    public List<AchievementDTO> getAchievements() { return achievements; }
    public void setAchievements(List<AchievementDTO> achievements) { this.achievements = achievements; }
}