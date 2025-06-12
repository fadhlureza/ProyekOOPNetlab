package com.proyekoprec.monsterpanicbackend.service;

import com.proyekoprec.monsterpanicbackend.dto.GameRecordRequest;
import com.proyekoprec.monsterpanicbackend.dto.LeaderboardDTO;
import com.proyekoprec.monsterpanicbackend.model.GameRecord;
import com.proyekoprec.monsterpanicbackend.model.Player;
import com.proyekoprec.monsterpanicbackend.repository.GameRecordRepository;
import com.proyekoprec.monsterpanicbackend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.util.List;

@Service
public class GameRecordService {

    @Autowired
    private GameRecordRepository gameRecordRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AchievementService achievementService;

    @Transactional
    public GameRecord saveRecord(String username, GameRecordRequest recordRequest) {
        Player player = playerRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Player not found with username: " + username));
        
        GameRecord newRecord = new GameRecord();
        newRecord.setPlayer(player);
        newRecord.setScore(recordRequest.getScore());
        newRecord.setGameMode(recordRequest.getGame_mode());
        newRecord.setMaxCombo(recordRequest.getMax_combo());

        GameRecord savedRecord = gameRecordRepository.save(newRecord);

        achievementService.checkAndUnlockAchievements(username, recordRequest);

        return savedRecord;
    }

    public List<LeaderboardDTO> getLeaderboard() {
        return gameRecordRepository.findTopScores();
    }
}