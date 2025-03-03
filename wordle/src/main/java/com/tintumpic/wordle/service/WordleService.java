package com.tintumpic.wordle.service;

import com.tintumpic.wordle.util.WordListLoader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WordleService {
    private List<String> wordList;
    private String wordOfTheDay;

    @PostConstruct
    public void init() {
        wordList = WordListLoader.loadWords("src/main/resources/wordle.txt");
        pickNewWord();
    }

    public void pickNewWord() {
        Collections.shuffle(wordList);
        wordOfTheDay = wordList.get(0);
        System.out.println("Word of the Day: " + wordOfTheDay);
    }

    public List<String> evaluateGuess(String guess) {
        guess.toLowerCase();
        if (guess.length() != 5) {
            throw new IllegalArgumentException("Guess must be exactly 5 letters.");
        }
        if (!wordList.contains(guess)) {
            throw new IllegalArgumentException("Word not in dictionary.");
        }

        List<String> feedback = new ArrayList<>(Collections.nCopies(5, "gray"));

        char[] target = wordOfTheDay.toCharArray();
        char[] guessChars = guess.toCharArray();

        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == target[i]) {
                feedback.set(i, "green");
                target[i] = '*';
                guessChars[i] = '_';
            }
        }

        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == '_') continue;
            for (int j = 0; j < 5; j++) {
                if (guessChars[i] == target[j]) {
                    feedback.set(i, "yellow");
                    target[j] = '*';
                    break;
                }
            }
        }

        return feedback;
    }
}
