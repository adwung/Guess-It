package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Scanner;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = { "playedBy", "puzzleId" },
        foreignKeys = {
            @ForeignKey(entity = Player.class,
                    parentColumns = "username",
                    childColumns = "playedBy",
                    onDelete = CASCADE),
            @ForeignKey(entity = Puzzle.class,
                    parentColumns = "uniqueId",
                    childColumns = "puzzleId",
                    onDelete = CASCADE)
        }
)
public class Game {
    //PK will be both playedBy and puzzleId
    //FK link to the player who played the game
    @NonNull
    private String playedBy = "";
    //FK link to the puzzle being played
    @NonNull
    private int puzzleId = 0;

    private String displayPhrase;

    private int wrongGuesses;

    //Changing this to an int ... Money class would be a pain with persistence
    private int totalPrize;

    @Ignore
    private List<Character> unquessedLetters;

    private boolean completed;

    public String getPlayedBy() {
        return playedBy;
    }

    public void setPlayedBy(String playedBy) {
        this.playedBy = playedBy;
    }

    public int getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getDisplayPhrase() {
        return displayPhrase;
    }

    public void setDisplayPhrase(String displayPhrase) {
        this.displayPhrase = displayPhrase;
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    public void setWrongGuesses(int wrongGuesses) {
        this.wrongGuesses = wrongGuesses;
    }

    public int getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(int totalPrize) {
        this.totalPrize = totalPrize;
    }

    public List<Character> getUnquessedLetters() {
        return unquessedLetters;
    }

    public void setUnquessedLetters(List<Character> unquessedLetters) {
        this.unquessedLetters = unquessedLetters;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}