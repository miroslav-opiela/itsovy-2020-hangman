package sk.itsovy.android.hangman;

public class HangmanGame implements Game {
    @Override
    public boolean isWon() {
        return false;
    }

    @Override
    public CharSequence getGuessedCharacters() {
        return null;
    }

    @Override
    public String getChallengeWord() {
        return null;
    }

    @Override
    public int getAttemptsLeft() {
        return 0;
    }

    @Override
    public boolean guess(char character) {
        return false;
    }
}
