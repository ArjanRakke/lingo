package com.bep.lingo.words;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Deze klasse zorgt er voor dat er een csv bestand wordt aangemaakt
    met gelfilterde woorden. Hierdoor ben je niet afhankelijk van één database
    en kan je gemakkelijk meerdere databases met woorden vullen.
*/
public class WordFilter {
    public static void main(String [] args)
    {
        //https://www.journaldev.com/709/java-read-file-line-by-line
        BufferedReader reader;
        try {
            String currDir = System.getProperty("user.dir");
            reader = new BufferedReader(new FileReader(currDir + "/words_unfiltered.txt"));
            FileWriter words = new FileWriter(currDir + "/words_filtered.csv");
            String line = reader.readLine();

            //https://www.tutorialspoint.com/java-program-to-check-if-a-string-contains-any-special-character
            //https://www.geeksforgeeks.org/check-if-a-string-contains-only-alphabets-in-java-using-regex/
            Pattern noSpecialChars = Pattern.compile("^[a-z]*$");
            Matcher matchNoSpecialChars;
            boolean check;

            while (line != null) {
                String word = line;
                matchNoSpecialChars = noSpecialChars.matcher(word);
                check = matchNoSpecialChars.find();

                if (word.length() >= 5 && word.length() <= 7 && check) {
                    words.write(word + "\n");
                }

                line = reader.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
