package com.bep.lingo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
//https://github.com/sastani/Hangman/blob/master/src/main/java/com/models/Game.java
public class Game {
    private final String gameId;

    @JsonIgnore
    private final Word word;

    private String guessedWord;
    private int guesses = 5;
    private int score = 0;

    @JsonIgnore
    private long timeOnGuess;

    public Game(Word word) {
        this.gameId = createId();
        this.word = word;
        this.guessedWord = wordHider(this.word.getWord().length());
        this.timeOnGuess = System.currentTimeMillis();
    }

    //hides the to be guessed word
    public static String wordHider(int word_len){
        String w;
        w = "_".repeat(Math.max(0, word_len));
        return w;
    }

    //generate id for game
    public static String createId(){
        String alphabet= "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        String id;
        Random r = new Random(System.currentTimeMillis());
        //generate random sequence of 6 chars
        int id_len = 6;
        char ran_char;
        for(int i=0; i < id_len; i++){
            //choose random letter in alphabet
            ran_char = alphabet.charAt(r.nextInt(26));
            sb.append(ran_char);
        }
        id = sb.toString();
        return id;
    }

    //https://github.com/sastani/Hangman/blob/master/src/main/java/com/web/GamesController.java
    //Get games in session
    public List<Game> getCurrentGames(HttpSession session) {
        List<Game> games = (List<Game>) session.getAttribute("games");

        if (games == null) {
            games = new ArrayList<>();
            session.setAttribute("games", games);
        }
        return games;
    }
}
