package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import edu.gatech.seclass.sdpguessit.data.Game;
import edu.gatech.seclass.sdpguessit.data.Puzzle;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;
import edu.gatech.seclass.sdpguessit.data.Tournament;
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThrough;

public class JoinTournamentActivity extends AppCompatActivity {

    private ArrayAdapter<String> customAdapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_tournament);

        List<Tournament> tournaments = SDPGuessItDatabase
                .getInstance(getApplicationContext())
                .tournamentDao()
                .getAllTournamentsPlayerCanPlay(((SDPGuessItApplication)getApplication()).getPlayer().getUsername());


        String[] strings = new String[tournaments.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = tournaments.get(i).getTournamentName();
        }

        customAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, strings);

        listView = findViewById(R.id.joinTournamentListView);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(mMessageClickedHandler);
    }

    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            String tournamentName = (String)parent.getItemAtPosition(position);

            int totalPrize = 0;

            List<Game> games = SDPGuessItDatabase
                    .getInstance(getApplicationContext())
                    .playThroughDao()
                    .getGamesAlreadyPlayedForPlayThrough(tournamentName, ((SDPGuessItApplication)getApplication()).getPlayer().getUsername());

            for (Game game : games) {
                totalPrize += game.getTotalPrize();
            }

            TournamentPlayThrough tpt = new TournamentPlayThrough();
            tpt.setCompleted(false);
            tpt.setPlayedBy(((SDPGuessItApplication)getApplication()).getPlayer().getUsername());
            tpt.setTotalPrize(totalPrize);
            tpt.setTournamentName(tournamentName);

            SDPGuessItDatabase
                    .getInstance(getApplicationContext())
                    .playThroughDao()
                    .insert(tpt);

            List<Puzzle> puzzles = SDPGuessItDatabase
                    .getInstance(getApplicationContext())
                    .playThroughDao()
                    .getPuzzlesNotPlayedForPlayThrough(tournamentName, ((SDPGuessItApplication)getApplication()).getPlayer().getUsername());

            Bundle bundle = new Bundle();
            bundle.putInt("puzzleId", puzzles.get(0).getUniqueId());
            Intent intent = new Intent(parent.getContext(), GameActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

}
