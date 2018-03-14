package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

import edu.gatech.seclass.sdpguessit.data.Puzzle;
import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;

public class createPuzzleActivity extends AppCompatActivity {
    private EditText puzzleID;
    private EditText puzzlePhrase;
    private Spinner guesses;
    private Button saveButton;
    private String userName;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_puzzle);

        puzzleID = findViewById(R.id.uniqueIDValue);
        puzzlePhrase = findViewById(R.id.phraseValue);
        guesses = findViewById(R.id.guessValue);
        saveButton = findViewById(R.id.savePuzzleButton);

        adapter = ArrayAdapter.createFromResource(this,R.array.guess_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guesses.setAdapter(adapter);
        guesses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(),adapterView.getItemIdAtPosition(i)+" selected", Toast.LENGTH_LONG);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void handleClick(View view) {
        if (view.getId() == R.id.savePuzzleButton) {
            String phrase;
            String guessCount;
            int uniqueID = 1000;
            phrase = puzzlePhrase.getText().toString();
            guessCount = guesses.getSelectedItem().toString();

            Player player = ((SDPGuessItApplication) this.getApplication()).getPlayer();
            userName = player.getUsername();

            Puzzle puzzle = new Puzzle();
            puzzle.setPhrase(phrase);
            puzzle.setMaxWrongGuesses(Integer.parseInt(guessCount));
            puzzle.getUniqueId();
            puzzle.setCreatedByUsername(userName);

            try {
                if(!phrase.isEmpty()) {
                    SDPGuessItDatabase.getInstance(this).puzzleDao().insert(puzzle);
                    List<Puzzle> puzzlesByUser = SDPGuessItDatabase.getInstance(this).puzzleDao().getPuzzlesByCreatedBy(userName);
                    int id = puzzlesByUser.get(puzzlesByUser.size() - 1).getUniqueId();
                    puzzleID.setText(Integer.toString(id));

                    Toast.makeText(getApplicationContext(), "Puzzle Created", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //Thread.sleep(1500);
                }
                else{
                    throw new IllegalArgumentException("No phrase detected");
                }
            }
            catch(Exception e){
                if (phrase.isEmpty()) {
                    puzzlePhrase.setError("Enter a Puzzle Phrase");
                } else {
                    InputMethodManager inManage = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inManage.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            }
        }

    }
}
