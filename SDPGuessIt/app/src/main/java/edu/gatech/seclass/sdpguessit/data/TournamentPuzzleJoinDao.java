package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TournamentPuzzleJoinDao {

    @Insert
    void insert(TournamentPuzzleJoin tournamentPuzzleJoin);

    @Query("SELECT * FROM Puzzle " +
            "INNER JOIN TournamentPuzzleJoin " +
            "ON Puzzle.uniqueId=TournamentPuzzleJoin.puzzleId " +
            "WHERE TournamentPuzzleJoin.tournamentName=:tournamentName"
    )
    List<Puzzle> getPuzzlesForTournament(String tournamentName);

    @Query("SELECT * FROM Tournament " +
            "INNER JOIN TournamentPuzzleJoin " +
            "ON Tournament.tournamentName=TournamentPuzzleJoin.tournamentName " +
            "WHERE TournamentPuzzleJoin.puzzleId=:puzzleId"
    )
    List<Tournament> getTournamentsForPuzzle(int puzzleId);
}
