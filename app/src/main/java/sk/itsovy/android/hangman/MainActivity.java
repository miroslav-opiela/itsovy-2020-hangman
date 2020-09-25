package sk.itsovy.android.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Game hangmanGame;

    private ImageView imageView;
    private TextView textView;
    private EditText editText;

    private static final String BUNDLE_KEY = "game";

    private final int[] gallowsIds = {
            R.drawable.gallows0,
            R.drawable.gallows1,
            R.drawable.gallows2,
            R.drawable.gallows3,
            R.drawable.gallows4,
            R.drawable.gallows5,
            R.drawable.gallows6,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageViewGallows);
        textView = findViewById(R.id.textViewGuessedWord);
        editText = findViewById(R.id.editTextLetter);

        if (savedInstanceState == null) {
            restartGame();
        } else {
            hangmanGame = (Game) savedInstanceState.getSerializable(BUNDLE_KEY);
            updateGallows();
            updateText();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(BUNDLE_KEY, hangmanGame);
    }

    public void onGallowsClick(View view) {
        if (hangmanGame.isWon() || hangmanGame.getAttemptsLeft() == 0) {
            restartGame();
            return;
        }

        CharSequence text = editText.getText();
        if (text == null || text.length() == 0) {
            Toast.makeText(this, "Insert a letter",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        char letter = Character.toLowerCase(text.charAt(0));

        if (letter < 'a' || letter > 'z') {
            Toast.makeText(this, "Only alphabetical allowed",
                    Toast.LENGTH_SHORT).show();
            editText.setText("");
            return;
        }


        boolean success = hangmanGame.guess(letter);
        editText.setText("");

        if (success) {
            updateText();
            if (hangmanGame.isWon()) {
                if (hangmanGame instanceof HangmanGame) {
                    HangmanGame hg = (HangmanGame) hangmanGame;
                    updateBestTime(hg.getTime());
                }
                alertWonGame();
            }
        } else {
            updateGallows();
            if (hangmanGame.getAttemptsLeft() == 0) {
                // PREHRA
                alertLostGame();
                textView.setText(hangmanGame.getChallengeWord());
            }
        }
    }

    private void updateText() {
        textView.setText(hangmanGame.getGuessedCharacters());
    }

    private void updateGallows() {
        int gallowsIdx = Game.DEFAULT_ATTEMPTS_LEFT - hangmanGame.getAttemptsLeft();
        imageView.setImageResource(gallowsIds[gallowsIdx]);

    }

    private void alertLostGame() {
        ColorFilter filter = new LightingColorFilter(Color.RED, Color.BLACK);
        imageView.setColorFilter(filter);
    }

    private void alertWonGame() {
        ColorFilter filter = new LightingColorFilter(Color.GREEN, Color.BLACK);
        imageView.setColorFilter(filter);

    }

    private void restartGame() {
        hangmanGame = new HangmanGame(new Random());
        textView.setText(hangmanGame.getGuessedCharacters());
        imageView.setColorFilter(null);
        imageView.setImageResource(gallowsIds[0]);
    }

    private void updateBestTime(long time) {
        // zapisovat do preferences
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        long bestTime = pref.getLong("BestTime", Long.MAX_VALUE);
        if (time < bestTime) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong("BestTime", time);
            editor.apply();
            newBestTime(time);
        }
    }

    private void newBestTime(long time) {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics

        builder.setMessage("Your time is " + time + "ms")
                .setTitle("Best time");

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}