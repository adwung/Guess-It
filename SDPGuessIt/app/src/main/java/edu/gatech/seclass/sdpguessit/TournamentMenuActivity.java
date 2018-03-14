package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TournamentMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_menu);
    }

    public void joinTournament(View view) {
        Intent intent = new Intent(this, JoinTournamentActivity.class);
        startActivity(intent);
    }

    public void continueTournament(View view) {
        Intent intent = new Intent(this, ContinueTournamentActivity.class);
        startActivity(intent);
    }
}
