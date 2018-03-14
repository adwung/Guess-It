package edu.gatech.seclass.sdpguessit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThrough;

public class PlayerTournamentStatisticsActivity extends AppCompatActivity {

    private ArrayAdapter<TournamentPlayThrough> customAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_tournament_statistics);



        List<TournamentPlayThrough> tpts = SDPGuessItDatabase
                .getInstance(this)
                .playThroughDao()
                .getCompletedPlayThroughsForPlayer(((SDPGuessItApplication)this.getApplication()).getPlayer().getUsername());

        TournamentPlayThrough[] playThroughs = tpts.toArray(new TournamentPlayThrough[tpts.size()]);

        customAdapter = new PlayerTournamentStatsArrayAdapter(this, playThroughs);

        listView = findViewById(R.id.playerTournamentStatsListView);
        listView.setAdapter(customAdapter);
    }
}
