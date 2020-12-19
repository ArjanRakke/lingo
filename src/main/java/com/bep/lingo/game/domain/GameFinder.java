package com.bep.lingo.game.domain;

import com.bep.lingo.game.domain.exception.GameDoesNotExistException;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Getter
public class GameFinder {

    //https://github.com/sastani/Hangman/blob/master/src/main/java/com/web/GamesController.java
    // Find an existing game
    public Game getGame(String id, HttpSession session) throws GameDoesNotExistException {
        List<Game> games = (List<Game>) session.getAttribute("games");
        Game g = null;
        if (games != null) {
            for(int i = 0; i < games.size(); i++) {
                g = games.get(i);
                if(g.getGameId().equals(id)){
                    break;
                }
            }
            if (g == null) {
                throw new GameDoesNotExistException(id);
            }
        }
        else {
            throw new GameDoesNotExistException(id);
        }

        return g;
    }
}
