package com.bep.lingo.game.presentation;

import com.bep.lingo.game.data.HighscoreRepository;
import com.bep.lingo.game.data.WordRepository;
import com.bep.lingo.game.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.beans.binding.When;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @DisplayName("Get all highscores")
    void getAllHighscores() throws Exception {

        HighScore highScore = new HighScore("James", 4);
        HighScore highScore2 = new HighScore("Arjan", 5);
        HighScore highScore3 = new HighScore("Ana", 4);
        HighScore highScore4 = new HighScore("Bob", 2);

        ArrayList list = new ArrayList();
        list.add(highScore);
        list.add(highScore2);
        list.add(highScore3);
        list.add(highScore4);

        Mockito.when(highscoreRepository.getAllHighScores()).thenReturn(list);

        mockMvc.perform(get("/lingo/highscores")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
