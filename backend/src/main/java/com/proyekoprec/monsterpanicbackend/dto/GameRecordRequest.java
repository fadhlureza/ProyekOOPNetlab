package com.proyekoprec.monsterpanicbackend.dto;

import jakarta.validation.constraints.NotNull;

public class GameRecordRequest {
    @NotNull
    private Integer score;
    @NotNull
    private String game_mode;
    private Integer max_combo;
    
    // Getter dan Setter
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getGame_mode() { return game_mode; }
    public void setGame_mode(String game_mode) { this.game_mode = game_mode; }
    public Integer getMax_combo() { return max_combo; }
    public void setMax_combo(Integer max_combo) { this.max_combo = max_combo; }
}