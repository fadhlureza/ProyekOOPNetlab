package com.proyekoprec.monsterpanicbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayerAchievementId implements Serializable {

    @Column(name = "player_id")
    private Integer playerId;

    @Column(name = "achievement_id")
    private Integer achievementId;

    public PlayerAchievementId() {}

    public PlayerAchievementId(Integer playerId, Integer achievementId) {
        this.playerId = playerId;
        this.achievementId = achievementId;
    }

    // Getter dan Setter
    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(Integer achievementId) {
        this.achievementId = achievementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerAchievementId that = (PlayerAchievementId) o;
        return Objects.equals(playerId, that.playerId) &&
            Objects.equals(achievementId, that.achievementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, achievementId);
    }
}