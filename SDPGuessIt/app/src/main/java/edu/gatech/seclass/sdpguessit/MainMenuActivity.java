package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.Puzzle;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void createPuzzle(View view) {
        Intent intent = new Intent(this, createPuzzleActivity.class);
        startActivity(intent);
    }

    public void solvePuzzle(View view) {
        Player currentPlayer = ((SDPGuessItApplication)this.getApplication()).getPlayer();
        List<Integer> puzzles = SDPGuessItDatabase
                .getInstance(this)
                .puzzleDao().getPuzzleIdsNotCreatedAndNotPlayedBy(currentPlayer.getUsername());
        if(puzzles == null || puzzles.size() == 0) {
            Toast.makeText(this, "There are no puzzles for this user to play.", Toast.LENGTH_LONG).show();
        } else {
            Random rand = new Random();
            Integer randomElement = puzzles.get(rand.nextInt(puzzles.size()));
            Bundle bundle = new Bundle();
            bundle.putInt("puzzleId", randomElement);
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void createTournament(View view) {
        Intent intent = new Intent(this, createTournamentActivity.class);
        startActivity(intent);
    }

    public void continueTournament(View view) {
        Intent intent = new Intent(this, TournamentMenuActivity.class);
        startActivity(intent);
    }

    public void viewStatistics(View view) {
        Intent intent = new Intent(this, StatisticsMenuActivity.class);
        startActivity(intent);
    }

    public void clearData(View view) {
        SDPGuessItDatabase.deleteDatabase(this);
        ((SDPGuessItApplication)this.getApplication()).setPlayer(null);
        Intent intent = new Intent(this, TitleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        ((SDPGuessItApplication)this.getApplication()).setPlayer(null);
        Intent intent = new Intent(this, TitleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
