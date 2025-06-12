package com.proyekoprec.monsterpanicbackend.controller;

import com.proyekoprec.monsterpanicbackend.dto.PlayerProfileDTO;
import com.proyekoprec.monsterpanicbackend.dto.UpdatePasswordRequest;
import com.proyekoprec.monsterpanicbackend.dto.UpdateUsernameRequest;
import com.proyekoprec.monsterpanicbackend.model.Player;
import com.proyekoprec.monsterpanicbackend.repository.PlayerRepository;
import com.proyekoprec.monsterpanicbackend.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/me/profile")
    public ResponseEntity<PlayerProfileDTO> getMyProfile(Authentication authentication) {
        Player currentPlayer = playerRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Player tidak ditemukan dari token."));
        
        PlayerProfileDTO profile = playerService.getPlayerProfile(currentPlayer.getPlayerId());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me/username")
    public ResponseEntity<?> updateMyUsername(@Valid @RequestBody UpdateUsernameRequest request, Authentication authentication) {
        String currentUsername = authentication.getName();
        playerService.updateUsername(currentUsername, request.getNewUsername());
        
        return ResponseEntity.ok(Map.of("message", "Username berhasil diperbarui. Silakan login kembali."));
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> updateMyPassword(@Valid @RequestBody UpdatePasswordRequest request, Authentication authentication) {
        String username = authentication.getName();
        playerService.updatePassword(username, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password berhasil diperbarui."));
    }
}
