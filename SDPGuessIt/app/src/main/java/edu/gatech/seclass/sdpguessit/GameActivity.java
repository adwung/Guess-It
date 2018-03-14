package edu.gatech.seclass.sdpguessit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.common.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import edu.gatech.seclass.sdpguessit.data.Game;
import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.Puzzle;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThrough;

public class GameActivity extends AppCompatActivity {

    //public static current_game;
    //CurrentGameStatus current_game = new CurrentGameStatus();

    // Utility Components
    private Random rand;

    // UI Components
    private TextView puzzlePhrase;
    private TextView text;
    private TextView totalPrize;
    private TextView guessesRemaining;
    private TextView availableLettersView;
    private TextView gamePrize;

    // Puzzle & Game Components
    private Puzzle puzzle;
    private int incorrectGuessesRemaining;
    private String displayString;
    private int prize;
    private ArrayList<String> availableLetters;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize Utility components
        rand = new Random();

        // Initialize UI components
        text = findViewById(R.id.text);
        puzzlePhrase = findViewById(R.id.puzzlePhrase);
        totalPrize = findViewById(R.id.gameTotalPrize);
        guessesRemaining = findViewById(R.id.guessesRemaining);
        availableLettersView = findViewById(R.id.availableLettersValues);
        gamePrize  = findViewById(R.id.gamePrize);

        // Fetch the passed in puzzle
        Bundle bundle = getIntent().getExtras();
        int puzzleId = bundle.getInt("puzzleId");
        puzzle = SDPGuessItDatabase
                .getInstance(this)
                .puzzleDao()
                .getPuzzleByUniqueId(puzzleId);

        if (puzzle == null) {
            Toast.makeText(getApplicationContext(), "No puzzles created. Returning to main menu", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        // Initialize game variables
        incorrectGuessesRemaining = puzzle.getMaxWrongGuesses();
        displayString = generateInitialDisplayPhrase(puzzle);
        prize = 0;
        amount = (rand.nextInt(10) + 1) * 100;
        availableLetters = new ArrayList();
        String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        for (int i = 0; i < letters.length; i++) {
            availableLetters.add(letters[i]);
        }


        // Populate UI
        puzzlePhrase.setText(displayString);
        totalPrize.setText(Integer.toString(prize));
        guessesRemaining.setText(Integer.toString(incorrectGuessesRemaining));
        availableLettersView.setText(buildAvailableLettersViewString());
        gamePrize.setText(Integer.toString(amount));
    }

    private String buildAvailableLettersViewString()
    {
        String toReturn = "";
        for (int i = 0; i < availableLetters.size(); i++) {
            toReturn += availableLetters.get(i);
            if (i != availableLetters.size() - 1) {
                toReturn += " ";
            }
        }
        return toReturn;
    }

    private String generateInitialDisplayPhrase(Puzzle puzzle) {
        String phrase = puzzle.getPhrase();
        int index = 1;
        while (index < phrase.length()) {
            phrase = phrase.substring(0, index) + " " + phrase.substring(index);
            index += 2;
        }
        return phrase.replaceAll("[a-zA-Z]", "_");
    }

    private String getUnparsedDisplayPhrase() {
        String toReturn = displayString;
        int index = 1;
        while (index < toReturn.length()) {
            toReturn = toReturn.substring(0, index) + toReturn.substring(index + 1);
            index++;
        }
        return toReturn;
    }

    public void guessConsonant(View view) {
        String letter = text.getText().toString();
        if (letter.length() != 1) {
            text.setError("Must be a single character");
        } else if (letter.equals("a")||letter.equals("e")||letter.equals("i")||letter.equals("o")||letter.equals("u")||letter.equals("A")||letter.equals("E")||letter.equals("I")||letter.equals("O")||letter.equals("U")) {
            text.setError("Must be a consonant");
        } else if (!letter.matches("[a-zA-Z]+")) {
            text.setError("Must be a letter");
        } else if (!availableLetters.contains(letter.toUpperCase())){
            text.setError("Letter is not available");
        } else {
            makeGuess(amount, letter.toUpperCase().charAt(0));
            amount = (rand.nextInt(10) + 1) * 100;
            gamePrize.setText(Integer.toString(amount));
        }
    }
    public void buyVowel(View view) {
        String letter = text.getText().toString();
        if (letter.length() != 1) {
            text.setError("Must be a single character");
        } else if (letter.isEmpty()) {
            text.setError("Must be a single character");
        }
        else if (letter.charAt(0)!='a' && letter.charAt(0)!='A' && letter.charAt(0)!='e' && letter.charAt(0)!='E' &&
            letter.charAt(0)!='i' && letter.charAt(0)!='I' && letter.charAt(0)!='o' && letter.charAt(0)!='O' &&
            letter.charAt(0)!='u' && letter.charAt(0)!='U') {
            text.setError("Must be a vowel");
        } else if (prize < 300) {
            text.setError("You don't have enough money left to buy a vowel");
        } else if (!availableLetters.contains(letter.toUpperCase())){
            text.setError("Letter is not available");
        } else {
            prize -= 300;
            totalPrize.setText(Integer.toString(prize));
            makeGuess(0, letter.toUpperCase().charAt(0));
        }
    }

    public void solvePuzzle(View view) {
        String completed = text.getText().toString();
        String solved = puzzle.getPhrase();
        int count =  getUnparsedDisplayPhrase().length() - getUnparsedDisplayPhrase().replace("_","").length();

        if (completed.toUpperCase().equals(solved.toUpperCase())) {
            prize += count*1000;
            Toast.makeText(getApplicationContext(), "Congratulations! You guessed " + solved + " correctly! You won " + prize + ". Returning you to main menu.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Sorry! You got it incorrect. Returning you to main menu", Toast.LENGTH_LONG).show();
            prize = 0;
            totalPrize.setText(Integer.toString(prize));
        }
        text.setText("");
        finishGame();
    }

    private void finishGame() {
        Game game = new Game();
        game.setCompleted(true);
        game.setDisplayPhrase(getUnparsedDisplayPhrase());
        game.setPlayedBy(((SDPGuessItApplication)this.getApplication()).getPlayer().getUsername());
        game.setPuzzleId(puzzle.getUniqueId());
        game.setTotalPrize(prize);
        game.setWrongGuesses(incorrectGuessesRemaining);

        SDPGuessItDatabase
                .getInstance(this)
                .gameDao()
                .insert(game);

        List<TournamentPlayThrough> tpts = SDPGuessItDatabase
                .getInstance(this)
                .playThroughDao()
                .getTournamentByTournamentAndPlayedByWithPuzzle(((SDPGuessItApplication)this.getApplication()).getPlayer().getUsername(), puzzle.getUniqueId());

        for (TournamentPlayThrough tpt : tpts) {
            tpt.setTotalPrize(tpt.getTotalPrize() + prize);
            SDPGuessItDatabase
                    .getInstance(this)
                    .playThroughDao()
                    .update(tpt);
        }

        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void makeGuess(int amount, char letter) {
        String originalPhrase = puzzle.getPhrase();
        String upperOriginal = originalPhrase.toUpperCase();
        int occurrences = 0;
        int index = upperOriginal.indexOf(letter);
        while (index >= 0) {
            occurrences++;
            if (upperOriginal.length() == 1) {
                displayString = originalPhrase;
                break;
            }
            if (index != 0) {
                if (index != upperOriginal.length() - 1) {
                    displayString = displayString.substring(0, 2 * index) + originalPhrase.charAt(index) + displayString.substring(2 * index + 1);
                } else {
                    displayString = displayString.substring(0, 2 * index) + originalPhrase.charAt(index);
                }
            } else {
                displayString = originalPhrase.charAt(index) + displayString.substring(1);
            }
            index = upperOriginal.indexOf(letter, index + 1);
        }
        availableLetters.remove(Character.toString(letter).toUpperCase());
        availableLettersView.setText(buildAvailableLettersViewString());
        if (occurrences != 0) {
            prize += occurrences * amount;
            puzzlePhrase.setText(displayString);
            totalPrize.setText(Integer.toString(prize));
            text.setText("");
            if (getUnparsedDisplayPhrase().toUpperCase().equals(puzzle.getPhrase().toUpperCase())) {
                Toast.makeText(getApplicationContext(), "Congratulations! You guessed " + puzzle.getPhrase() + " correctly! You won " + prize + ". Returning you to main menu.", Toast.LENGTH_LONG).show();
                finishGame();
            }

        } else {
            incorrectGuessesRemaining--;
            guessesRemaining.setText(Integer.toString(incorrectGuessesRemaining));
            text.setText("");
            if (incorrectGuessesRemaining <= 0) {
                Toast.makeText(getApplicationContext(), "Sorry! You have lost the game. Returning you back to main menu.", Toast.LENGTH_LONG).show();
                finishGame();
            }
        }
    }

    public void backMainMenu(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("This will cause you to forfeit the game");

        builder.setPositiveButton("Exit to main menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                prize = 0;
                totalPrize.setText(Integer.toString(prize));
                finishGame();
                startActivity(new Intent(getBaseContext(), MainMenuActivity.class));


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //cancel = false;
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("This will cause you to forfeit the game")
                .setPositiveButton("Exit to main menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        arg0.dismiss();
                        prize = 0;
                        totalPrize.setText(Integer.toString(prize));
                        finishGame();
                        startActivity(new Intent(getBaseContext(), MainMenuActivity.class));


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                })
                .show();
    }
}