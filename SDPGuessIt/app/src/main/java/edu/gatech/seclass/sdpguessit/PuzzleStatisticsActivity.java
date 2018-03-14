package edu.gatech.seclass.sdpguessit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.gatech.seclass.sdpguessit.data.Game;
import edu.gatech.seclass.sdpguessit.data.GameDao;
import edu.gatech.seclass.sdpguessit.data.Puzzle;
import edu.gatech.seclass.sdpguessit.data.PuzzleDao;
import edu.gatech.seclass.sdpguessit.data.PuzzleStatisticsItem;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;

public class PuzzleStatisticsActivity extends AppCompatActivity {

    private ArrayAdapter<PuzzleStatisticsItem> customAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_statistics);

        ArrayList<PuzzleStatisticsItem> stats = buildStats();
        PuzzleStatisticsItem[] statsArray = stats.toArray(new PuzzleStatisticsItem[stats.size()]);

        customAdapter = new PuzzleStatsArrayAdapter(this, statsArray);

        listView = findViewById(R.id.puzzleStatsListView);
        listView.setAdapter(customAdapter);
    }

    private ArrayList<PuzzleStatisticsItem> buildStats() {
        SDPGuessItDatabase db = SDPGuessItDatabase.getInstance(this);
        GameDao gameDao = db.gameDao();
        PuzzleDao puzzleDao = db.puzzleDao();


        List<Puzzle> puzzles = puzzleDao.getAllPuzzles();

        ArrayList<PuzzleStatisticsItem> stats = new ArrayList<>(puzzles.size());
        for (Puzzle puzzle: puzzles) {
            PuzzleStatisticsItem stat = new PuzzleStatisticsItem();
            stat.puzzleId = puzzle.getUniqueId();
            Integer played = gameDao.getPlayerCountForPuzzle(puzzle.getUniqueId());
            if(played == null || played == 0) {
                stat.playersPlayed = 0;
                stat.maxScore = 0;
                stat.maxScoreUser = "Unplayed";
            } else {
                stat.playersPlayed = played;
                Game max = gameDao.getTopScoringGameForPuzzle(puzzle.getUniqueId());
                if(max == null) {
                    Toast.makeText(this, "Problem occured building statistics", Toast.LENGTH_LONG).show();
                } else {
                    stat.maxScore = max.getTotalPrize();
                    stat.maxScoreUser = max.getPlayedBy();
                }
            }
            stats.add(stat);
        }
        return stats;
    }
}
