package com.bep.lingo.game.domain;

import com.bep.lingo.game.data.WordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
class GameUnitTests {

    @MockBean
    private WordRepository wordRepository;

    private static final String fiveLetterWord = "fives";
    private static final String sixLetterWord = "worlds";
    private static final String sevenLetterWord = "ability";

    Word wFive = new Word(fiveLetterWord);
    Word wSix = new Word(sixLetterWord);
    Word wSeven = new Word(sevenLetterWord);

    @Test
    @DisplayName("Hide a five letter word")
    void wordHiderFiveLetterWord() {
        Game game = new Game(wFive);

        assertEquals(game.getGuessedWord(), wFive.getWord().toCharArray()[0] + "____");
    }

    @Test
    @DisplayName("Hide a six letter word")
    void wordHiderSixLetterWord() {
        Game game = new Game(wSix);

        assertEquals(game.getGuessedWord(), wSix.getWord().toCharArray()[0] + "_____");
    }

    @Test
    @DisplayName("Hide a seven letter word")
    void wordHiderSevenLetterWord() {
        Game game = new Game(wSeven);

        assertEquals(game.getGuessedWord(), wSeven.getWord().toCharArray()[0] + "______");
    }

    @Test
    @DisplayName("Generate a game id")
    void createId() {
        Game game = new Game(wFive);

        assertNotNull(game.getGameId());
    }

    @Test
    @DisplayName("Games found")
    void getCurrentGamesGamesFound() {
        MockHttpSession session = new MockHttpSession();
      
        Game game = new Game(wFive);
        StartedGame sGame = new StartedGame(game);

        ArrayList<StartedGame> sgArray = new ArrayList<>();
        sgArray.add(sGame);

        session.setAttribute("games", sgArray);

        List<Game> gList = game.getCurrentGames(session);

        assertEquals(session.getAttribute("games"), gList);
    }

    @Test
    @DisplayName("No games found")
    void getCurrentGamesNoGamesFound() {
        MockHttpSession session = new MockHttpSession();

        Game game = new Game(wFive);

        List<Game> gList = game.getCurrentGames(session);

        assertEquals(session.getAttribute("games"), gList);
    }
  
    @Test
    @DisplayName("Checks the word for correct, present and absent letters")
    void wordChecker() {
        Game game = new Game(wFive);
        String guess = "vibes";

        String check = game.wordChecker(game, guess);

        assertEquals("[v: present, i: correct, b: absent, e: correct, s: correct]", check);
    }

    @Test
    @DisplayName("Set letters at correct position")
    void setGuessedWord() {
        Game game = new Game(wFive);

        String word = "vibes";

        game.setGuessedWord(word);

        assertEquals("fi_es", game.getGuessedWord());
    }
}
