package com.bep.lingo.game.presentation;

import com.bep.lingo.game.data.WordRepository;
import com.bep.lingo.game.domain.*;
import com.bep.lingo.game.domain.exception.GameDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/lingo")
//https://github.com/sastani/Hangman/blob/master/src/main/java/com/web/GamesController.java
public class GameController {
    private final WordRepository wordRepository;

    private final GameFinder finder;

    public GameController(WordRepository wordRepository, GameFinder finder) {
        this.wordRepository = wordRepository;
        this.finder = finder;
    }

    //Create new lingo game
    @PostMapping("/game")
    public StartedGame newGame(HttpSession session) {
        Word word = wordRepository.getFiveLetterWord();
        Game game = new Game(word);
        List<Game> games = game.getCurrentGames(session);
        games.add(game);
        return new StartedGame(game);
    }

    //Get all games
    @GetMapping("/games")
    public List<Game> getGameList(HttpSession session) {
        return (List<Game>) session.getAttribute("games");
    }

    //Make a guess
    @PostMapping("/guess")
    public ResponseEntity<?> makeGuess (@RequestBody Guess gameAndWord, HttpSession session) throws GameDoesNotExistException {
        String game = gameAndWord.getGame();
        String guess = gameAndWord.getGuess();
        Game g = finder.getGame(game, session);
        String gameId = g.getGameId();
        String tbgWord = g.getWord().getWord();
        GameStatus status = g.getStatus();
        if (!(status == null)) {
            switch (status) {
                case ACTIVE:
                    break;
                case INACTIVE:
                    return g.gameOver(gameId);
            }
        }

        if (gameId.equals(game)) {

            if (System.currentTimeMillis() <= g.getTimeOnGuess() + 10000) {

                if(guess.length() == g.getWord().getWord().length()) {

                    if (guess.matches("^[a-z]*$")) {
                        String check = g.wordChecker(g, guess);
                        g.setGuessedWord(guess);
                        g.setWordChecker(check);
                        g.setTimeOnGuess(System.currentTimeMillis());
                        g.subtractGuess();
                        g.setStatus();

                        if (tbgWord.equals(g.getGuessedWord())) {

                            if (g.getRoundSetting() == 2) {
                                Word word = wordRepository.getSixLetterWord();
                                g.setWord(word);
                                g.resetGuessedWord();

                                StartedGame newRound = new StartedGame(g);

                                return new ResponseEntity<>(newRound, HttpStatus.OK);
                            }
                            else if (g.getRoundSetting() == 3) {
                                Word word = wordRepository.getSevenLetterWord();
                                g.setWord(word);
                                g.resetGuessedWord();
                                g.setRoundSetting(0);

                                StartedGame newRound = new StartedGame(g);

                                return new ResponseEntity<>(newRound, HttpStatus.OK);
                            }
                            else {
                                Word word = wordRepository.getFiveLetterWord();
                                g.setWord(word);
                                g.resetGuessedWord();

                                StartedGame newRound = new StartedGame(g);

                                return new ResponseEntity<>(newRound, HttpStatus.OK);
                            }
                        }
                    }
                    else {
                        g.subtractGuess();
                        g.setStatus();
                        g.setTimeOnGuess(System.currentTimeMillis());
                        return g.invalidWord(guess);
                    }
                }
                else {
                    g.subtractGuess();
                    g.setStatus();
                    g.setTimeOnGuess(System.currentTimeMillis());
                    return g.invalidWord(guess);
                }
            }
            else {
                g.subtractGuess();
                g.setStatus();
                g.setTimeOnGuess(System.currentTimeMillis());
                return g.timeOverLimit();
            }
        }
        else {
            return g.gameDoesNotExist(game);
        }
        Feedback f = new Feedback(g);

        return new ResponseEntity<>(f, HttpStatus.OK);
    }
}
