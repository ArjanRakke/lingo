package com.bep.lingo.game.domain;

import com.bep.lingo.game.data.WordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
class GameUnitTests {

    @Autowired
    private MockMvc mockMvc;

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

    //https://www.logicbig.com/how-to/code-snippets/jcode-spring-mvc-mockhttpsession.html
    @Test
    @DisplayName("Get all games")
    void getCurrentGames() throws Exception {
        MockHttpSession session = new MockHttpSession();

        when(wordRepository.getFiveLetterWord()).thenReturn(wFive);

        Game game = new Game(wFive);
        StartedGame sGame = new StartedGame(game);

        ArrayList<StartedGame> sgArray = new ArrayList<>();
        sgArray.add(sGame);

        session.setAttribute("games", sgArray);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/lingo/games")
                .session(session).accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk()).andDo(print());

    }
}
