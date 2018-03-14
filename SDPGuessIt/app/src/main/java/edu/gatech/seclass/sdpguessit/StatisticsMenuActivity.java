package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StatisticsMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_menu);
    }
    public void playerPuzzleStatistics(View view) {
        Intent intent = new Intent(this, PlayerPuzzleStatisticsActivity.class);
        startActivity(intent);
    }

    public void playerTournamentStatistics(View view) {
        Intent intent = new Intent(this, PlayerTournamentStatisticsActivity.class);
        startActivity(intent);
    }

    public void puzzleStatistics(View view) {
        Intent intent = new Intent(this, PuzzleStatisticsActivity.class);
        startActivity(intent);
    }

    public void tournamentStatistics(View view) {
        Intent intent = new Intent(this, TournamentStatisticsActivity.class);
        startActivity(intent);
    }
}
