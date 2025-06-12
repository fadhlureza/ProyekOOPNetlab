package com.proyekoprec.monsterpanicbackend.service;

import com.proyekoprec.monsterpanicbackend.dto.PlayerProfileDTO;
import com.proyekoprec.monsterpanicbackend.model.Player;
import com.proyekoprec.monsterpanicbackend.repository.GameRecordRepository;
import com.proyekoprec.monsterpanicbackend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private GameRecordRepository gameRecordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AchievementService achievementService;

    public void updateUsername(String currentUsername, String newUsername) {
        if (playerRepository.existsByUsername(newUsername)) {
            throw new RuntimeException("Username '" + newUsername + "' sudah digunakan.");
        }
        Player player = playerRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Player tidak ditemukan."));
        player.setUsername(newUsername);
        playerRepository.save(player);
    }
    
    public void updatePassword(String username, String currentPassword, String newPassword) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Player tidak ditemukan."));

        if (!passwordEncoder.matches(currentPassword, player.getPassword())) {
            throw new RuntimeException("Password saat ini salah.");
        }
        
        player.setPassword(passwordEncoder.encode(newPassword));
        playerRepository.save(player);
    }

    public PlayerProfileDTO getPlayerProfile(Integer playerId) {
        PlayerProfileDTO profile = new PlayerProfileDTO();
        profile.setTotalGames(gameRecordRepository.countByPlayerPlayerId(playerId));
        profile.setHighestScore(gameRecordRepository.findTopScoreByPlayerId(playerId).orElse(0));
        profile.setAchievements(achievementService.getPlayerAchievements(playerId));
        return profile;
    }
}