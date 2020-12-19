package com.bep.lingo.game.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "highscore", schema = "lingo")
@Getter
public class HighScore {

    @Id
    @Column(name = "player")
    private String player;
    @Column(name = "score")
    private int score;

    public HighScore() {

    }

    public HighScore(String player, int score) {
            this.player = player;
            this.score = score;
    }


}
