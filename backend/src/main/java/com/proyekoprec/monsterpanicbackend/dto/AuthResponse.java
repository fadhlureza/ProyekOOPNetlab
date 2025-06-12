package com.proyekoprec.monsterpanicbackend.dto;

public class AuthResponse {
    private String token;
    private Integer playerId;
    private String username;

    public AuthResponse(String token, Integer playerId, String username) {
        this.token = token;
        this.playerId = playerId;
        this.username = username;
    }
    
    // Getter dan Setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}