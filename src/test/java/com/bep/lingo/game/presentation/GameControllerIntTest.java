package com.bep.lingo.game.presentation;

import com.bep.lingo.game.data.WordRepository;
import com.bep.lingo.game.domain.Game;
import com.bep.lingo.game.domain.Word;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordRepository wordRepository;

    @MockBean
    protected MockHttpSession session;

    @Test
    void newGame() throws Exception {

        Word w = new Word(1L, "fives");

        Mockito.when(wordRepository.getFiveLetterWord()).thenReturn(w);

        mockMvc.perform(post("/lingo/game")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.score", Matchers.is(0)))
                .andExpect(jsonPath("$.guesses", Matchers.is(5)))
                .andExpect(jsonPath("$.word", Matchers.is("_____")))
                .andExpect(jsonPath("$.*", Matchers.hasSize(4)))
                .andDo(print());
    }

    @Test
    void getGameList() throws Exception {
        this.mockMvc.perform(get("/lingo/games"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
