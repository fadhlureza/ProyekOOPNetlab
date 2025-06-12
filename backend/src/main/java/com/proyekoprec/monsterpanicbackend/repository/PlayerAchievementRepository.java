package com.proyekoprec.monsterpanicbackend.repository;

import com.proyekoprec.monsterpanicbackend.model.Achievement; // <-- IMPORT YANG DIPERBAIKI
import com.proyekoprec.monsterpanicbackend.model.PlayerAchievement;
import com.proyekoprec.monsterpanicbackend.model.PlayerAchievementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerAchievementRepository extends JpaRepository<PlayerAchievement, PlayerAchievementId> {

    @Query("SELECT pa.achievement FROM PlayerAchievement pa WHERE pa.player.playerId = :playerId")
    List<Achievement> findAchievementsByPlayerId(Integer playerId);
}