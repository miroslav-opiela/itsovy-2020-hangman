package sk.itsovy.android.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Game hangmanGame;

    private ImageView imageView;
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageViewGallows);
        textView = findViewById(R.id.textViewGuessedWord);
        editText = findViewById(R.id.editTextLetter);

        hangmanGame = new HangmanGame();
    }

    public void onGallowsClick(View view) {
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


        hangmanGame.guess(letter);
        editText.setText("");
    }
}