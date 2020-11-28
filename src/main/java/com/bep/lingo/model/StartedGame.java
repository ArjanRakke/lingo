package com.bep.lingo.model;

import lombok.Data;

@Data
public class StartedGame {
    private final String gameId;
    private int score;
    private int guesses;
    private String word;
    private long timeSinceLastGuess;

    public StartedGame(Game game) {
        gameId = game.getGameId();
        score = game.getScore();
        guesses = game.getGuesses();
        word = game.getGuessedWord();
    }

}
