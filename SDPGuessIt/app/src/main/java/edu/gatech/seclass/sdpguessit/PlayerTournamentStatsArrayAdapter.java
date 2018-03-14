package edu.gatech.seclass.sdpguessit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.gatech.seclass.sdpguessit.data.TournamentPlayThrough;


public class PlayerTournamentStatsArrayAdapter extends ArrayAdapter<TournamentPlayThrough> {
    private final Context context;
    private final TournamentPlayThrough[] values;

    public PlayerTournamentStatsArrayAdapter(Context context, TournamentPlayThrough[] values) {
        super(context, R.layout.player_tournament_stats_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.player_tournament_stats_layout, parent, false);

        TextView nameView = rowView.findViewById(R.id.playerTournamentStatsLayoutName);
        TextView valueView = rowView.findViewById(R.id.playerTournamentStatsLayoutValue);

        nameView.setText(values[position].getTournamentName());
        valueView.setText("$" + Integer.toString(values[position].getTotalPrize()));
        // Change the icon for Windows and iPhone

        return rowView;
    }
}
