package edu.gatech.seclass.sdpguessit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.sdpguessit.data.Tournament;
import edu.gatech.seclass.sdpguessit.data.TournamentDao;
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThrough;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThroughDao;
import edu.gatech.seclass.sdpguessit.data.TournamentStatisticsItem;


public class TournamentStatisticsActivity extends AppCompatActivity {

    private ArrayAdapter<TournamentStatisticsItem> customAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_statistics);

        ArrayList<TournamentStatisticsItem> stats = buildStats();
        TournamentStatisticsItem[] statsArray = stats.toArray(new TournamentStatisticsItem[stats.size()]);

        customAdapter = new TournamentStatsArrayAdapter(this, statsArray);

        listView = findViewById(R.id.tournamentListView2);
        listView.setAdapter(customAdapter);
    }

    private ArrayList<TournamentStatisticsItem> buildStats() {
        SDPGuessItDatabase db = SDPGuessItDatabase.getInstance(this);
        TournamentPlayThroughDao tptDao = db.playThroughDao();
        TournamentDao tournamentDao = db.tournamentDao();


        List<Tournament> tournaments = tournamentDao.getAllTournaments();

        ArrayList<TournamentStatisticsItem> stats = new ArrayList<>(tournaments.size());
        for (Tournament tournament: tournaments) {
            TournamentStatisticsItem stat = new TournamentStatisticsItem();
            stat.tournamentName = tournament.getTournamentName();
            Integer played = tptDao.getCountOfCompletedPlayThroughsForTournament(tournament.getTournamentName());
            if(played == null || played == 0) {
                stat.playersPlayed = 0;
                stat.maxScore = 0;
                stat.maxScoreUser = "Unplayed";
            } else {
                stat.playersPlayed = played;
                TournamentPlayThrough max = tptDao.getTopScoringCompletedPlaythroughForTournament(tournament.getTournamentName());
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
