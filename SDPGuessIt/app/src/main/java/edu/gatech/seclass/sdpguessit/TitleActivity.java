package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;

public class TitleActivity extends AppCompatActivity {

    private EditText usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        usernameText = findViewById(R.id.titleUsername);
    }

    public void login(View view) {
        String username = usernameText.getText().toString();
        if(username.isEmpty()){
            usernameText.setError("Username required to login");
            return;
        }

        Player player = SDPGuessItDatabase
                .getInstance(this)
                .playerDao()
                .getPlayerByUsername(username);

        if(player == null) {
            usernameText.setError("This username does not exist");
            return;
        }

        ((SDPGuessItApplication)this.getApplication()).setPlayer(player);
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void createUser(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }
}
