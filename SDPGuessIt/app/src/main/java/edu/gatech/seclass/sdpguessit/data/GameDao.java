package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GameDao {
    @Insert
    void insert(Game... games);

    @Update
    void update(Game... games);

    @Delete
    void delete(Game... games);

    @Query("SELECT * FROM game")
    List<Game> getAllGames();

    @Query("SELECT * FROM game WHERE playedBy = :playedBy")
    List<Game> getAllGamesForPlayer(String playedBy);

    @Query("SELECT * FROM game WHERE puzzleId = :puzzleId")
    List<Game> getAllGamesForPuzzle(int puzzleId);

    @Query("SELECT * FROM game WHERE playedBy = :playedBy AND puzzleId = :puzzleId")
    Game getGameByPayerAndPuzzle(String playedBy, int puzzleId);

    @Query("SELECT * FROM game WHERE puzzleId = :puzzleId " +
            " ORDER BY totalPrize DESC, wrongGuesses ASC LIMIT 1")
    Game getTopScoringGameForPuzzle(int puzzleId);

    @Query("SELECT count(*) FROM game WHERE puzzleId = :puzzleId")
    Integer getPlayerCountForPuzzle(int puzzleId);


}
