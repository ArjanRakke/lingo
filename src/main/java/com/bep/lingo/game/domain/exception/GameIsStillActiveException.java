package com.bep.lingo.game.domain.exception;

public class GameIsStillActiveException extends Exception {
    public GameIsStillActiveException(String id){
        super(String.format("Game with id: %s is still active. You cannot register a score from an active game!", id));
    }
}
