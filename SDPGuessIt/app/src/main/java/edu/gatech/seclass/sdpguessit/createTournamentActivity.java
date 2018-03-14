package edu.gatech.seclass.sdpguessit;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.CheckedTextView;
import android.view.View;
import android.content.Context;
import android.widget.Toast;

import edu.gatech.seclass.sdpguessit.data.Tournament;
import edu.gatech.seclass.sdpguessit.data.Puzzle;
import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThrough;
import edu.gatech.seclass.sdpguessit.data.TournamentPuzzleJoin;

public class createTournamentActivity extends AppCompatActivity {

    private Button createButton;
    private EditText tournamentName;
    private ListView list;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private String userName;
    private ArrayList<Integer> checkedPuzzles = new ArrayList<>();
    private ArrayList<String> puzzleList = new ArrayList<>();
    private int notCheckedCount = 0;
    private int checkedCount = 0;
    LinearLayout linearMain;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);

        Player player = ((SDPGuessItApplication)this.getApplication()).getPlayer();
        userName = player.getUsername().toString();


        List<Puzzle> puzzles = SDPGuessItDatabase.getInstance(this).puzzleDao().getPuzzlesCreatedOrPlayedBy(userName);

        for(Puzzle puzzle: puzzles) {
            puzzleList.add(Integer.toString(puzzle.getUniqueId()));
        }

        list = (ListView) findViewById(R.id.tournamentListView);
        tournamentName = findViewById(R.id.tournamentNameValue);
        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(createTournamentActivity.this,android.R.layout.simple_list_item_checked, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg) {
                // TODO Auto-generated method stub
                if (!list.isItemChecked(position)) {
                    list.setItemChecked(position, false);
                } else {
                    list.setItemChecked(position, true);
                }
            }
        });

        for(int i=0; i < puzzleList.size();i++) {

            // this line adds the data of your EditText and puts in your array
            arrayList.add(puzzleList.get(i));
            adapter.notifyDataSetChanged();
        }
    }

    public void createTournament(View view) {

        // Validate a name was entered
        String tournament_name = tournamentName.getText().toString();
        if (tournament_name.isEmpty()) {
            tournamentName.setError("Enter a Tournament name");
            return;
        }

        // Make sure the tournament name doesn't already exist
        Tournament nameCheck = SDPGuessItDatabase.getInstance(this).tournamentDao().getTournamentByName(tournament_name);
        if (nameCheck != null) {
            tournamentName.setError("A Tournament already exists with that name");
            return;
        }

        // List of puzzles for the tournament
        ArrayList<TournamentPuzzleJoin> tpjs = new ArrayList<>();

        // Populate the list of puzzles based off those selected in the UI
        for (int i = 0; i < puzzleList.size(); i++) {
            if (list.isItemChecked(i)) {
                TournamentPuzzleJoin joinTournamentData = new TournamentPuzzleJoin();
                joinTournamentData.setPuzzleId(Integer.parseInt(puzzleList.get(i)));
                joinTournamentData.setTournamentName(tournament_name);
                tpjs.add(joinTournamentData);
            }
        }

        // Make sure at least one puzzle is selected
        if (tpjs.size() == 0) {
            tournamentName.setError("No puzzles selected");
            return;
        }

        // Make sure more than 5 puzzles were not selected
        if (tpjs.size() > 5) {
            tournamentName.setError("A Tournament cannot have more than 5 puzzles");
            return;
        }

        // Save the tournament
        Tournament tournament = new Tournament();
        tournament.setCreatedByUsername(userName);
        tournament.setTournamentName(tournament_name);
        SDPGuessItDatabase.getInstance(this).tournamentDao().insert(tournament);

        // Associate each puzzle the tournament
        for (TournamentPuzzleJoin tpj : tpjs) {
            SDPGuessItDatabase.getInstance(this).tournamentPuzzleJoinDao().insert(tpj);
        }

        Toast.makeText(getApplicationContext(), "Tournament Created", Toast.LENGTH_SHORT).show(); //display in long period of time
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}