package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;

public class NewUserActivity extends AppCompatActivity {

    private EditText firstNameText;
    private EditText lastNameText;
    private EditText usernameText;
    private EditText emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        firstNameText = findViewById(R.id.newUserFirstNameVal);
        lastNameText = findViewById(R.id.newUserLastNameVal);
        usernameText = findViewById(R.id.newUserUsernameVal);
        emailText = findViewById(R.id.newUserEmailVal);
    }

    public void createUser(View view) {
        String firstName, lastName, username, email;

        firstName = firstNameText.getText().toString();
        lastName = lastNameText.getText().toString();
        username = usernameText.getText().toString();
        email = emailText.getText().toString();

        boolean valid = true;
        if(firstName.isEmpty()) {
            firstNameText.setError("First Name required");
            valid = false;
        }

        if(lastName.isEmpty()) {
            lastNameText.setError("Last Name required");
            valid = false;
        }

        if(username.isEmpty()) {
            usernameText.setError("Username required");
            valid = false;
        }

        if(email.isEmpty()) {
            emailText.setError("Email required");
            valid = false;
        }

        if(!valid) {
            Toast.makeText(this, "All fields are required to create a new user.", Toast.LENGTH_SHORT).show();
            return;
        }

        Player player = new Player();
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setUsername(username);
        player.setEmail(email);

        try {
            SDPGuessItDatabase
                    .getInstance(this)
                    .playerDao()
                    .insert(player);
        } catch (SQLiteConstraintException e) {
            usernameText.setError("Username already taken");
            Toast.makeText(this, "Requested username is already used", Toast.LENGTH_SHORT).show();
            return;
        }

        ((SDPGuessItApplication)this.getApplication()).setPlayer(player);
        Toast.makeText(this, "New User Created", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void backToTitle(View view) {
        Intent intent = new Intent(this, TitleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
