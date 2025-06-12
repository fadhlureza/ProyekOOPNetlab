package com.proyekoprec.monsterpanicbackend.service;

import com.proyekoprec.monsterpanicbackend.dto.AuthRequest;
import com.proyekoprec.monsterpanicbackend.dto.AuthResponse;
import com.proyekoprec.monsterpanicbackend.model.Player;
import com.proyekoprec.monsterpanicbackend.repository.PlayerRepository;
import com.proyekoprec.monsterpanicbackend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse login(AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        Player player = playerRepository.findByUsername(loginRequest.getUsername()).get();
        return new AuthResponse(jwt, player.getPlayerId(), player.getUsername());
    }

    public Player register(AuthRequest registerRequest) {
        if (playerRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        Player player = new Player();
        player.setUsername(registerRequest.getUsername());
        player.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        return playerRepository.save(player);
    }
}