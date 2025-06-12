package com.proyekoprec.monsterpanicbackend.repository;

import com.proyekoprec.monsterpanicbackend.dto.LeaderboardDTO;
import com.proyekoprec.monsterpanicbackend.model.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, Integer> {

    @Query("SELECT new com.proyekoprec.monsterpanicbackend.dto.LeaderboardDTO(p.username, gr.score, gr.gameMode) " +
           "FROM GameRecord gr JOIN gr.player p " +
           "ORDER BY gr.score DESC")
    List<LeaderboardDTO> findTopScores();
    
    long countByPlayerPlayerId(Integer playerId);
    
    @Query("SELECT MAX(g.score) FROM GameRecord g WHERE g.player.playerId = :playerId")
    Optional<Integer> findTopScoreByPlayerId(Integer playerId);
}