package com.proyekoprec.monsterpanicbackend.repository;

import com.proyekoprec.monsterpanicbackend.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
}
