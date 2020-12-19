package com.bep.lingo.game.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartedGame {
    private final String gameId;
    private GameStatus status;
    private int score;
    private int guesses;
    private String word;

    public StartedGame(Game game) {
        gameId = game.getGameId();
        status = game.getStatus();
        score = game.getScore();
        guesses = game.getGuesses();
        word = game.getGuessedWord();
    }

}
