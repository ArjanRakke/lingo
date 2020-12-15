package com.bep.lingo.game.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feedback {
    private final String gameId;
    private GameStatus status;
    private int score;
    private int guesses;
    private String word;
    private String wordChecker;

    public Feedback(Game game) {
        gameId = game.getGameId();
        status = game.getStatus();
        score = game.getScore();
        guesses = game.getGuesses();
        word = game.getGuessedWord();
        wordChecker = game.getWordChecker();
    }
}
