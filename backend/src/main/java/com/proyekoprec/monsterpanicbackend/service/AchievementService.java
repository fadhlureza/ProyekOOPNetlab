package com.proyekoprec.monsterpanicbackend.service;

import com.proyekoprec.monsterpanicbackend.dto.AchievementDTO;
import com.proyekoprec.monsterpanicbackend.dto.GameRecordRequest;
import com.proyekoprec.monsterpanicbackend.model.Achievement;
import com.proyekoprec.monsterpanicbackend.model.Player;
import com.proyekoprec.monsterpanicbackend.model.PlayerAchievement;
import com.proyekoprec.monsterpanicbackend.repository.AchievementRepository;
import com.proyekoprec.monsterpanicbackend.repository.PlayerAchievementRepository;
import com.proyekoprec.monsterpanicbackend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private PlayerAchievementRepository playerAchievementRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public void checkAndUnlockAchievements(String username, GameRecordRequest gameResult) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Player not found"));

        List<Achievement> allAchievements = achievementRepository.findAll();
        List<Achievement> unlockedAchievements = playerAchievementRepository.findAchievementsByPlayerId(player.getPlayerId());

        for (Achievement achievement : allAchievements) {
            if (unlockedAchievements.stream().noneMatch(a -> a.getAchievementId().equals(achievement.getAchievementId()))) {
                boolean unlocked = false;
                switch (achievement.getName()) {
                    case "Skor Pertama":
                        unlocked = true; // Selalu terbuka setelah game pertama
                        break;
                    case "Combo Master":
                        if (gameResult.getMax_combo() != null && gameResult.getMax_combo() >= 20) {
                            unlocked = true;
                        }
                        break;
                    case "Pemain Profesional":
                        if (gameResult.getScore() >= 1000) {
                            unlocked = true;
                        }
                        break;
                }

                if (unlocked) {
                    PlayerAchievement pa = new PlayerAchievement(player, achievement);
                    playerAchievementRepository.save(pa);
                }
            }
        }
    }

    public List<AchievementDTO> getPlayerAchievements(Integer playerId) {
        List<Achievement> achievements = playerAchievementRepository.findAchievementsByPlayerId(playerId);
        return achievements.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private AchievementDTO convertToDto(Achievement achievement) {
        AchievementDTO dto = new AchievementDTO();
        dto.setName(achievement.getName());
        dto.setDescription(achievement.getDescription());
        dto.setIcon_class(achievement.getIconClass());
        return dto;
    }
}