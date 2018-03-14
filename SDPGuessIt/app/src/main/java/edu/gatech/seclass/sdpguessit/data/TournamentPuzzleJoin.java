package edu.gatech.seclass.sdpguessit.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = { "tournamentName", "puzzleId" },
        foreignKeys = {
                @ForeignKey(entity = Tournament.class,
                        parentColumns = "tournamentName",
                        childColumns = "tournamentName",
                        onDelete = CASCADE),
                @ForeignKey(entity = Puzzle.class,
                        parentColumns = "uniqueId",
                        childColumns = "puzzleId",
                        onDelete = CASCADE)
        }
)
public class TournamentPuzzleJoin {
        @NonNull
        String tournamentName = "";
        @NonNull
        int puzzleId = 0;

        public String getTournamentName() {
                return tournamentName;
        }

        public void setTournamentName(String tournamentName) {
                this.tournamentName = tournamentName;
        }

        public int getPuzzleId() {
                return puzzleId;
        }

        public void setPuzzleId(int puzzleId) {
                this.puzzleId = puzzleId;
        }
}
