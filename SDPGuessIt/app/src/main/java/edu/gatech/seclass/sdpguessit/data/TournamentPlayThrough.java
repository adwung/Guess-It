package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = { "playedBy", "tournamentName" },
        foreignKeys = {
                @ForeignKey(entity = Player.class,
                        parentColumns = "username",
                        childColumns = "playedBy",
                        onDelete = CASCADE),
                @ForeignKey(entity = Tournament.class,
                        parentColumns = "tournamentName",
                        childColumns = "tournamentName",
                        onDelete = CASCADE),
        }
)
public class TournamentPlayThrough {

    //PK will be both playedBy and tournamentName
    //FK link to the player who played the game
    @NonNull
    private String playedBy = "";
    //FK link to the tournament being played to name field
    @NonNull
    private String tournamentName = "";

    private int totalPrize;

    private boolean completed;

    public String getPlayedBy() {
        return playedBy;
    }

    public void setPlayedBy(String playedBy) {
        this.playedBy = playedBy;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public int getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(int totalPrize) {
        this.totalPrize = totalPrize;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
