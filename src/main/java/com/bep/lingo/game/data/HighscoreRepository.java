package com.bep.lingo.game.data;

import com.bep.lingo.game.domain.HighScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HighscoreRepository extends JpaRepository<HighScore, Long> {
    @Modifying
    @Query(value = "INSERT INTO highscore (player, score) VALUES (:player , :score)", nativeQuery = true)
    @Transactional
    void registerHighscore(@Param("player") String player, @Param("score") int score);
    @Query(value = "SELECT player, score FROM highscore ORDER BY score DESC;", nativeQuery = true)
    List<HighScore> getAllHighScores();
}
