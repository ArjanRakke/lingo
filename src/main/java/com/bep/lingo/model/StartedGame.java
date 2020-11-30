package com.bep.lingo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartedGame {
    private final String gameId;
    private int score;
    private int guesses;
    private String word;

    public StartedGame(Game game) {
        gameId = game.getGameId();
        score = game.getScore();
        guesses = game.getGuesses();
        word = game.getGuessedWord();
    }

}
