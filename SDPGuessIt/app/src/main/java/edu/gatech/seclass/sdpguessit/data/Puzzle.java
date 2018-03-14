package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Player.class,
                parentColumns = "username",
                childColumns = "createdByUsername"
        )
)
public class Puzzle {

    @PrimaryKey(autoGenerate = true)
    private int uniqueId = 0;

    private String phrase;
    private int maxWrongGuesses;

    //FK link to Player via username
    private String createdByUsername;

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public int getMaxWrongGuesses() {
        return maxWrongGuesses;
    }

    public void setMaxWrongGuesses(int maxWrongGuesses) {
        this.maxWrongGuesses = maxWrongGuesses;
    }
}
