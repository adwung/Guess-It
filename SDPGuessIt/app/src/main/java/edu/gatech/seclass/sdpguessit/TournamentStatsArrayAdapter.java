package edu.gatech.seclass.sdpguessit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.gatech.seclass.sdpguessit.data.TournamentStatisticsItem;


public class TournamentStatsArrayAdapter extends ArrayAdapter<TournamentStatisticsItem> {
    private final Context context;
    private final TournamentStatisticsItem[] values;

    public TournamentStatsArrayAdapter(Context context, TournamentStatisticsItem[] values) {
        super(context, R.layout.tournament_stats_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tournament_stats_layout, parent, false);

        TextView nameView = rowView.findViewById(R.id.tournamentStatsLayoutName);
        TextView numberOfPlayersView = rowView.findViewById(R.id.tournamentStatsLayoutNumberPlayers);
        TextView topScoreView = rowView.findViewById(R.id.tournamentStatsLayoutTopScore);

        nameView.setText(values[position].tournamentName);
        numberOfPlayersView.setText("Number of Players: " + Integer.toString(values[position].playersPlayed));
        topScoreView.setText("Top Score: " + values[position].maxScoreUser + " - $" + Integer.toString(values[position].maxScore));
        // Change the icon for Windows and iPhone

        return rowView;
    }
}
