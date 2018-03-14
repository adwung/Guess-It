package edu.gatech.seclass.sdpguessit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.gatech.seclass.sdpguessit.data.Game;
import edu.gatech.seclass.sdpguessit.data.PuzzleStatisticsItem;


public class PuzzleStatsArrayAdapter extends ArrayAdapter<PuzzleStatisticsItem> {
    private final Context context;
    private final PuzzleStatisticsItem[] values;

    public PuzzleStatsArrayAdapter(Context context, PuzzleStatisticsItem[] values) {
        super(context, R.layout.puzzle_stats_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.puzzle_stats_layout, parent, false);

        TextView nameView = rowView.findViewById(R.id.puzzleStatsLayoutName);
        TextView numberOfPlayersView = rowView.findViewById(R.id.puzzleStatsLayoutNumberPlayers);
        TextView topScoreView = rowView.findViewById(R.id.puzzleStatsLayoutTopScore);

        nameView.setText(Integer.toString(values[position].puzzleId));
        numberOfPlayersView.setText("Number of Players: " + Integer.toString(values[position].playersPlayed));
        topScoreView.setText("Top Score: " + values[position].maxScoreUser + " - $" + Integer.toString(values[position].maxScore));
        // Change the icon for Windows and iPhone

        return rowView;
    }
}
