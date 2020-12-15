package com.bep.lingo.game.domain;

import com.bep.lingo.game.domain.exception.GameDoesNotExistException;
import com.bep.lingo.game.domain.exception.GameOverException;
import com.bep.lingo.game.domain.exception.InvalidWordException;
import com.bep.lingo.game.domain.exception.TimeOverLimitException;
import com.bep.lingo.game.presentation.GameDoesNotExistInfo;
import com.bep.lingo.game.presentation.GameOverInfo;
import com.bep.lingo.game.presentation.InvalidWordInfo;
import com.bep.lingo.game.presentation.TimeOverLimitInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;
import java.util.*;

@Getter
@Setter
//https://github.com/sastani/Hangman/blob/master/src/main/java/com/models/Game.java
public class Game {
    private String gameId;

    @JsonIgnore
    private Word word;

    private String guessedWord;
    private GameStatus status;
    private int guesses = 5;
    private int score = 0;

    @JsonIgnore
    private String wordChecker;

    @JsonIgnore
    private long timeOnGuess;

    @JsonIgnore
    private int roundSetting = 1;

    public Game(Word word) {
        this.gameId = createId();
        this.word = word;
        this.guessedWord = wordHider(this.word.getWord().length());
        this.status = GameStatus.ACTIVE;
        this.wordChecker = getWordChecker();
        this.timeOnGuess = System.currentTimeMillis();
    }

    //hides the to be guessed word
    public String wordHider(int word_len) {
        String w;
        w = word.getWord().toCharArray()[0] + "_".repeat(Math.max(0, (word_len - 1)));
        return w;
    }

    public void subtractGuess() {
        this.guesses--;
    }

    public void resetGuessedWord() {
        this.guessedWord = wordHider(this.word.getWord().length());
    }

    //generate id for game
    public static String createId() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        String id;
        Random r = new Random(System.currentTimeMillis());
        //generate random sequence of 6 chars
        int id_len = 6;
        char ran_char;
        for (int i = 0; i < id_len; i++) {
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

    //Checks if a letter is correct, present or absent in a word
    public String wordChecker(Game g, String guessedWord) {
        ArrayList<String> resultOfCheck = new ArrayList<>();
        String tbgw = g.getWord().getWord();
        String w;
        String[] toBeGuessedW = tbgw.split("(?!^)");
        String[] guessedW = guessedWord.split("(?!^)");
        ArrayList<String> toBeCheckedLetters = new ArrayList<>();

        for (int i = 0; i < toBeGuessedW.length; i++) {
            toBeCheckedLetters.add(toBeGuessedW[i]);
        }

        for (int i = 0; i < toBeGuessedW.length; i++) {
            if (toBeGuessedW[i].equals(guessedW[i])) {
                resultOfCheck.add(guessedW[i] + ": correct");

                for (int j = 0; j < toBeCheckedLetters.size(); j++) {
                    if (toBeCheckedLetters.get(j).contains(guessedW[i])) {
                        toBeCheckedLetters.remove(j);
                        break;
                    }
                }
            }
            else if (toBeCheckedLetters.contains(guessedW[i])) {
                resultOfCheck.add(guessedW[i] + ": present");

                for (int j = 0; j < toBeCheckedLetters.size(); j++) {

                    if (toBeCheckedLetters.get(j).contains(guessedW[i])) {
                        toBeCheckedLetters.remove(j);
                        break;
                    }
                }
            }
            else {
                resultOfCheck.add(guessedW[i] + ": absent");
            }
        }

        w = resultOfCheck.toString();

        return w;
    }

    //https://github.com/sastani/Hangman/blob/master/src/main/java/com/models/Game.java
    //Makes the correctly guessed letters visible for the player
    public void setGuessedWord(String gWord) {
        ArrayList<Integer> charInd = new ArrayList<>();

        String corrWord = this.word.getWord();

        for (int i = 0; i < corrWord.length(); i++) {
            Character cw = corrWord.charAt(i);
            Character gw = gWord.charAt(i);
            if (cw.equals(gw)) {
                charInd.add(i);
            }
        }
        String newGuessedWord;
        StringBuilder sb = new StringBuilder();

        String guessedWord = getGuessedWord();
        int numChars = guessedWord.length();
        for (int i = 0; i < numChars; i++) {
            if (charInd.contains(i)) {
                sb.append(gWord.toCharArray()[i]);
            } else {
                sb.append(guessedWord.charAt(i));
            }
        }
        newGuessedWord = sb.toString();
        this.guessedWord = newGuessedWord;
    }

    public void setStatus() {
        if (word.getWord().equals(guessedWord)) {
            this.guesses = 5;
            this.score = score + 1;
            this.roundSetting = roundSetting + 1;
            this.status = GameStatus.ACTIVE;
        } else if (guesses != 0) {
            this.status = GameStatus.ACTIVE;
        } else {
            this.status = GameStatus.INACTIVE;
        }
    }

    //https://github.com/sastani/Hangman/blob/master/src/main/java/com/models/Game.java
    //exception handler for dealing with games that do not exist
    @ExceptionHandler(GameDoesNotExistException.class)
    public ResponseEntity<GameDoesNotExistInfo> gameDoesNotExist(String id) {
        String s = String.format("Game with id: %s does not exist.", id);
        GameDoesNotExistInfo error = new GameDoesNotExistInfo(s);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //exception handler for dealing with games that are not active
    @ExceptionHandler(GameOverException.class)
    public ResponseEntity<GameOverInfo> gameOver(String id) {
        String s = String.format("Game with id: %s is already complete. please input your name to register your score.", id);
        GameOverInfo error = new GameOverInfo(s);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //exception handler for dealing with words that are invalid
    @ExceptionHandler(InvalidWordException.class)
    public ResponseEntity<InvalidWordInfo> invalidWord(String w) {
        String s = String.format("Guessed word %s is invalid.", w);
        InvalidWordInfo error = new InvalidWordInfo(s);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //exception handler for dealing with guesses made outside of the time limit
    @ExceptionHandler(TimeOverLimitException.class)
    public ResponseEntity<TimeOverLimitInfo> timeOverLimit() {
        String s = "You took too long to guess! You must make a guess within 10 seconds!";
        TimeOverLimitInfo error = new TimeOverLimitInfo(s);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
