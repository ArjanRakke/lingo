package com.bep.lingo.game.presentation;

import com.bep.lingo.game.data.HighscoreRepository;
import com.bep.lingo.game.domain.*;
import com.bep.lingo.game.domain.exception.GameDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/lingo")
public class HighscoreController {
    private final HighscoreRepository highscoreRepository;

    private final GameFinder finder;

    public HighscoreController(HighscoreRepository highscoreRepository, GameFinder finder) {
        this.highscoreRepository = highscoreRepository;
        this.finder = finder;
    }

    //Register highscore
    @PostMapping("/highscore")
    public ResponseEntity<?> registerHighscore(@RequestBody RegisterHighScore registerHighscore, HttpSession session) throws GameDoesNotExistException {
        String game = registerHighscore.getGame();
        String player = registerHighscore.getPlayer();
        Game g = finder.getGame(game, session);
        GameStatus status = g.getStatus();

        if (g.getGameId().equals(game)) {

            if (status == GameStatus.INACTIVE) {
                highscoreRepository.registerHighscore(player, g.getScore());
            }
            else {
                return g.gameIsStillActive(g.getGameId());
            }
        }
        else {
            return g.gameDoesNotExist(game);
        }



        HighScore highscore = new HighScore(player, g.getScore());

        return new ResponseEntity<>(highscore, HttpStatus.OK);
    }
}
