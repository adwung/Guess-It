package edu.gatech.seclass.sdpguessit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.gatech.seclass.sdpguessit.data.Game;


public class PlayerPuzzleStatsArrayAdapter extends ArrayAdapter<Game> {
    private final Context context;
    private final Game[] values;

    public PlayerPuzzleStatsArrayAdapter(Context context, Game[] values) {
        super(context, R.layout.player_puzzle_stats_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.player_puzzle_stats_layout, parent, false);

        TextView nameView = rowView.findViewById(R.id.playerPuzzleStatsLayoutName);
        TextView valueView = rowView.findViewById(R.id.playerPuzzleStatsLayoutValue);

        nameView.setText(Integer.toString(values[position].getPuzzleId()));
        valueView.setText("$" + Integer.toString(values[position].getTotalPrize()));
        // Change the icon for Windows and iPhone

        return rowView;
    }
}
