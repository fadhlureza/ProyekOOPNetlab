package com.proyekoprec.monsterpanicbackend.controller;

import com.proyekoprec.monsterpanicbackend.dto.GameRecordRequest;
import com.proyekoprec.monsterpanicbackend.dto.LeaderboardDTO;
import com.proyekoprec.monsterpanicbackend.service.AchievementService;
import com.proyekoprec.monsterpanicbackend.service.GameRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class GameRecordController {

    @Autowired
    private GameRecordService gameRecordService;
    
    @Autowired
    private AchievementService achievementService;

    @PostMapping("/records")
    public ResponseEntity<?> submitRecord(@Valid @RequestBody GameRecordRequest recordRequest, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        gameRecordService.saveRecord(userDetails.getUsername(), recordRequest);
        return ResponseEntity.ok("Record saved successfully");
    }

    @GetMapping("/records/leaderboard")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard() {
        return ResponseEntity.ok(gameRecordService.getLeaderboard());
    }
}