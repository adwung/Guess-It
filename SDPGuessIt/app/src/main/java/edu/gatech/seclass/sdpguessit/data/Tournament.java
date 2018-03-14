package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Player.class,
                parentColumns = "username",
                childColumns = "createdByUsername"
        )
)
public class Tournament {

    @PrimaryKey
    @NonNull
    private String tournamentName = "";

    //FK link to player - username
    private String createdByUsername;

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String name) {
        this.tournamentName = name;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }
}
