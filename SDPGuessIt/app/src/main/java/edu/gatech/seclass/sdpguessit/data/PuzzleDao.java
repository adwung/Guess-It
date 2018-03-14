package edu.gatech.seclass.sdpguessit.data;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PuzzleDao {

    @Insert
    void insert(Puzzle... puzzles);

    @Update
    void update(Puzzle... puzzles);

    @Delete
    void delete(Puzzle... puzzles);

    @Query("SELECT * FROM puzzle")
    List<Puzzle> getAllPuzzles();

    @Query("SELECT max(uniqueId) FROM puzzle")
    int getLastPuzzleId();

    @Query("SELECT * FROM puzzle WHERE uniqueId = :uniqueId")
    Puzzle getPuzzleByUniqueId(int uniqueId);

    @Query("SELECT * FROM puzzle WHERE createdByUsername = :createdByUsername")
    List<Puzzle> getPuzzlesByCreatedBy(String createdByUsername);

    @Query("SELECT * FROM puzzle " +
            "INNER JOIN game " +
            "ON game.puzzleId = puzzle.uniqueId " +
            "WHERE game.playedBy = :playedBy"
    )
    List<Puzzle> getPuzzlesByGamesPlayedBy(String playedBy);

    @Query("SELECT * FROM puzzle " +
            "WHERE createdByUsername = :username " +
            "OR EXISTS( " +
            "SELECT * FROM game " +
            "WHERE game.puzzleId = puzzle.uniqueId " +
            "AND game.playedBy = :username )"
    )
    List<Puzzle> getPuzzlesCreatedOrPlayedBy(String username);


    @Query("SELECT uniqueId FROM puzzle " +
            "WHERE createdByUsername = :username " +
            "OR EXISTS( " +
            "SELECT * FROM game " +
            "WHERE game.puzzleId = puzzle.uniqueId " +
            "AND game.playedBy = :username )"
    )
    List<Integer> getPuzzleIdsCreatedOrPlayedBy(String username);


    @Query("SELECT uniqueId FROM puzzle " +
            "WHERE createdByUsername <> :username " +
            "AND NOT EXISTS( " +
            "SELECT * FROM game " +
            "WHERE game.puzzleId = puzzle.uniqueId " +
            "AND game.playedBy = :username )"
    )
    List<Integer> getPuzzleIdsNotCreatedAndNotPlayedBy(String username);
}

