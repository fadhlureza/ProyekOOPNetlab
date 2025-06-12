package com.proyekoprec.monsterpanicbackend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "player_achievements")
public class PlayerAchievement {

    @EmbeddedId
    private PlayerAchievementId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playerId")
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("achievementId")
    @JoinColumn(name = "achievement_id")
    private Achievement achievement;

    @Column(name = "unlocked_at", nullable = false, updatable = false)
    private Instant unlockedAt = Instant.now();
    
    public PlayerAchievement() {}

    public PlayerAchievement(Player player, Achievement achievement) {
        this.player = player;
        this.achievement = achievement;
        this.id = new PlayerAchievementId(player.getPlayerId(), achievement.getAchievementId());
    }


    // Getter dan Setter
    public PlayerAchievementId getId() {
        return id;
    }

    public void setId(PlayerAchievementId id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public Instant getUnlockedAt() {
        return unlockedAt;
    }

    public void setUnlockedAt(Instant unlockedAt) {
        this.unlockedAt = unlockedAt;
    }
}