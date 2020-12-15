package com.bep.lingo.game.presentation;

import com.bep.lingo.game.data.WordRepository;
import com.bep.lingo.game.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
class GameControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordRepository wordRepository;

    @Test
    @DisplayName("Create a new game")
    void newGame() throws Exception {

        Word w = new Word("fives");

        Mockito.when(wordRepository.getFiveLetterWord()).thenReturn(w);

        mockMvc.perform(post("/lingo/game")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.status", Matchers.is("ACTIVE")))
                .andExpect(jsonPath("$.score", Matchers.is(0)))
                .andExpect(jsonPath("$.guesses", Matchers.is(5)))
                .andExpect(jsonPath("$.word", Matchers.is(w.getWord().toCharArray()[0] + "____")))
                .andDo(print());
    }

    @Test
    @DisplayName("Get all games")
    void getGameList() throws Exception {
        Word w = new Word("fives");

        MockHttpSession session = new MockHttpSession();

        Game game = new Game(w);
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

    //https://www.logicbig.com/how-to/code-snippets/jcode-spring-mvc-mockhttpsession.html
    @Test
    @DisplayName("Make a successful guess")
    void makeGuessIsSuccessful() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");

        String gameId = game.getGameId();
        String guess = "vibse";

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        //https://stackoverflow.com/questions/20504399/testing-springs-requestbody-using-spring-mockmvc
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Guess on non existent game")
    void makeGuessGameDoesNotExist() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");

        String gameId = "kfheka";
        String guess = w.getWord();

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("Guess with invalid characters")
    void makeGuessInvalidCharacters() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");

        String gameId = "agehav";
        String guess = "vib@s";

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("Guess with invalid word")
    void makeGuessInvalidWord() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");

        String gameId = game.getGameId();
        String guess = "four";

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("Guess on inactive game")
    void makeGuessGameIsOver() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");
        game.setStatus(GameStatus.INACTIVE);

        String gameId = "agehav";
        String guess = w.getWord();

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("Guess while over time limit")
    void makeGuessOverTimeLimit() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");

        String gameId = "agehav";
        String guess = w.getWord();

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        //https://www.quora.com/How-do-I-get-a-Java-program-to-wait-before-running-the-next-line-of-code-in-a-simple-hello-world-type-program
        Thread.sleep(10000);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("New round with six letter word")
    void makeGuessNewRoundSixLetterWord() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("vibes");
        Word w2 = new Word("checks");

        Game game = new Game(w);
        game.setGameId("agehav");

        String gameId = game.getGameId();
        String guess = w.getWord();

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        //https://stackoverflow.com/questions/20504399/testing-springs-requestbody-using-spring-mockmvc
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        Mockito.when(wordRepository.getSixLetterWord()).thenReturn(w2);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.gameId", Matchers.is("agehav")))
                .andExpect(jsonPath("$.status", Matchers.is("ACTIVE")))
                .andExpect(jsonPath("$.score", Matchers.is(1)))
                .andExpect(jsonPath("$.guesses", Matchers.is(5)))
                .andExpect(jsonPath("$.word", Matchers.is(w2.getWord().toCharArray()[0] + "_____")))
                .andDo(print());
    }

    @Test
    @DisplayName("New round with seven letter word")
    void makeGuessNewRoundSevenLetterWord() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("checks");
        Word w2 = new Word("swagger");

        Game game = new Game(w);
        game.setGameId("agehav");
        game.setRoundSetting(2);

        String gameId = game.getGameId();
        String guess = w.getWord();

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        //https://stackoverflow.com/questions/20504399/testing-springs-requestbody-using-spring-mockmvc
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        Mockito.when(wordRepository.getSevenLetterWord()).thenReturn(w2);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.gameId", Matchers.is("agehav")))
                .andExpect(jsonPath("$.status", Matchers.is("ACTIVE")))
                .andExpect(jsonPath("$.score", Matchers.is(1)))
                .andExpect(jsonPath("$.guesses", Matchers.is(5)))
                .andExpect(jsonPath("$.word", Matchers.is(w2.getWord().toCharArray()[0] + "______")))
                .andDo(print());
    }

    @Test
    @DisplayName("New round with five letter word")
    void makeGuessNewRoundFiveLetterWord() throws Exception {
        MockHttpSession session = new MockHttpSession();

        Word w = new Word("swagger");
        Word w2 = new Word("vibes");

        Game game = new Game(w);
        game.setGameId("agehav");
        game.setRoundSetting(0);

        String gameId = game.getGameId();
        String guess = w.getWord();

        ArrayList<Game> gArray = new ArrayList<>();
        gArray.add(game);

        session.setAttribute("games", gArray);

        Guess gameAndWord = new Guess();
        gameAndWord.setGame(gameId);
        gameAndWord.setGuess(guess);

        //https://stackoverflow.com/questions/20504399/testing-springs-requestbody-using-spring-mockmvc
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(gameAndWord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/lingo/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .session(session);

        Mockito.when(wordRepository.getFiveLetterWord()).thenReturn(w2);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.gameId", Matchers.is("agehav")))
                .andExpect(jsonPath("$.status", Matchers.is("ACTIVE")))
                .andExpect(jsonPath("$.score", Matchers.is(1)))
                .andExpect(jsonPath("$.guesses", Matchers.is(5)))
                .andExpect(jsonPath("$.word", Matchers.is(w2.getWord().toCharArray()[0] + "____")))
                .andDo(print());
    }
}
