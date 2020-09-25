package sk.itsovy.android.hangman;

import android.os.SystemClock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HangmanGame implements Game {

    private long startTime;

    /**
     * Pocet ostavajucich pokusov
     */
    private int triesLeft = DEFAULT_ATTEMPTS_LEFT;

    private final List<String> words = Arrays.asList("gallows", "hangman", "computer",
            "android", "developer");

    private String word;

    private StringBuilder guessed;

    public HangmanGame(Random random) {
        int index = random.nextInt(words.size());
        word = words.get(index);

        // vytvorit stringbuilder
        guessed = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            guessed.append(UNGUESSED_CHAR);
        }
        startTime = SystemClock.elapsedRealtime();
    }

    public long getTime() {
        return SystemClock.elapsedRealtime() - startTime;
    }

    @Override
    public boolean isWon() {
        return guessed.toString().equals(word);
    }

    @Override
    public CharSequence getGuessedCharacters() {
        return guessed;
    }

    @Override
    public String getChallengeWord() {
        return word;
    }

    @Override
    public int getAttemptsLeft() {
        return triesLeft;
    }

    @Override
    public boolean guess(char character) {
        if (triesLeft <= 0) {
            throw new IllegalStateException("No attempts left.");
        }
        boolean success = false;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == character) {
                guessed.setCharAt(i, character);
                success = true;
            }
        }
        if (!success) {
            // Neuhadli sme
            triesLeft--;
        }
        return success;
    }
}
