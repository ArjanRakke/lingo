package com.bep.lingo.controller;

import com.bep.lingo.model.Game;
import com.bep.lingo.model.StartedGame;
import com.bep.lingo.model.Word;
import com.bep.lingo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/lingo")
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    //Create new lingo game
    @PostMapping("/new")
    public StartedGame newGame(HttpSession session) {
        Long wordId = gameRepository.getFiveLetterWord().getWordId();
        String word = gameRepository.getFiveLetterWord().getWord();
        Word w = new Word(wordId, word);
        Game game = new Game(w);
        List<Game> games = game.getCurrentGames(session);
        games.add(game);
        return new StartedGame(game);
    }

    //Get all games
    @GetMapping("/games")
    public List<Game> getGameList(HttpSession session) {
        return (List<Game>) session.getAttribute("games");
    }
}
