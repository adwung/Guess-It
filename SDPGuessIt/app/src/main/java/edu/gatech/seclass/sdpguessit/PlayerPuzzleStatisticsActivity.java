package edu.gatech.seclass.sdpguessit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import edu.gatech.seclass.sdpguessit.data.Game;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;

public class PlayerPuzzleStatisticsActivity extends AppCompatActivity {

    private ArrayAdapter<Game> customAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_puzzle_statistics);



        List<Game> playedGames = SDPGuessItDatabase
                .getInstance(this)
                .gameDao()
                .getAllGamesForPlayer(((SDPGuessItApplication)this.getApplication()).getPlayer().getUsername());

        Game[] games = playedGames.toArray(new Game[playedGames.size()]);

        customAdapter = new PlayerPuzzleStatsArrayAdapter(this, games);

        listView = findViewById(R.id.playerPuzzleStatsListView);
        listView.setAdapter(customAdapter);
    }
}
