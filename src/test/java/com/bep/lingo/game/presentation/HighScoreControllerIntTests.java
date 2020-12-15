package com.bep.lingo.game.presentation;

import com.bep.lingo.game.data.HighscoreRepository;
import com.bep.lingo.game.data.WordRepository;
import com.bep.lingo.game.domain.Game;
import com.bep.lingo.game.domain.GameStatus;
import com.bep.lingo.game.domain.RegisterHighScore;
import com.bep.lingo.game.domain.Word;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
class HighScoreControllerIntTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameController gameController;

    @MockBean
    private WordRepository wordRepository;

    @MockBean
    private HighscoreRepository highscoreRepository;

    @Test
    @DisplayName("Succesfully register highscore")
    void registerHighScoreSuccessfully() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");
        game.setStatus(GameStatus.INACTIVE);
        game.setScore(3);

        RegisterHighScore registerHighScore = new RegisterHighScore();
        registerHighScore.setGame(game.getGameId());
        registerHighScore.setPlayer("Lingoplayer");

        //https://stackoverflow.com/questions/20504399/testing-springs-requestbody-using-spring-mockmvc
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(registerHighScore);

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/highscore")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        this.mockMvc.perform(builder)
                    .andExpect(MockMvcResultMatchers.status()
                    .isOk()).andDo(print());
    }

    @Test
    @DisplayName("Register score on active game")
    void registerHighScoreOnActiveGame() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");
        game.setStatus(GameStatus.ACTIVE);
        game.setScore(3);

        RegisterHighScore registerHighScore = new RegisterHighScore();
        registerHighScore.setGame(game.getGameId());
        registerHighScore.setPlayer("Lingoplayer");

        //https://stackoverflow.com/questions/20504399/testing-springs-requestbody-using-spring-mockmvc
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(registerHighScore);

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/highscore")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        this.mockMvc.perform(builder)
                    .andExpect(MockMvcResultMatchers.status()
                    .is4xxClientError()).andDo(print());
    }

    @Test
    @DisplayName("Register score on non existent game")
    void registerHighScoreOnNonExistentGame() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");
        game.setScore(3);

        RegisterHighScore registerHighScore = new RegisterHighScore();
        registerHighScore.setGame("sdhesf");
        registerHighScore.setPlayer("Lingoplayer");

        //https://stackoverflow.com/questions/20504399/testing-springs-requestbody-using-spring-mockmvc
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(registerHighScore);

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/highscore")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        this.mockMvc.perform(builder)
                    .andExpect(MockMvcResultMatchers.status()
                    .is4xxClientError()).andDo(print());
    }
}
