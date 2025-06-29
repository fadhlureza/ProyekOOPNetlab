package com.proyekoprec.monsterpanicbackend.repository;

import com.proyekoprec.monsterpanicbackend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByUsername(String username);
    Boolean existsByUsername(String username);
}