package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TournamentDao {

    @Insert
    void insert(Tournament... tournaments);

    @Update
    void update(Tournament... tournaments);

    @Delete
    void delete(Tournament... tournaments);

    @Query("SELECT * FROM tournament")
    List<Tournament> getAllTournaments();

    @Query("SELECT * FROM tournament WHERE createdByUsername = :createdByUsername")
    List<Tournament> getAllTournamentsByCreatedBy(String createdByUsername);

    @Query("SELECT * FROM tournament WHERE tournamentName = :tournamentName")
    Tournament getTournamentByName(String tournamentName);

    //No puzzles created by the player
    //Also, at least one puzzle not yet played.
    @Query("SELECT * FROM tournament t " +
            " WHERE NOT EXISTS( " +
            "   SELECT * FROM puzzle pc " +
            "   INNER JOIN TournamentPuzzleJoin jc " +
            "   ON pc.uniqueId = jc.puzzleId " +
            "   WHERE pc.createdByUsername = :username " +
            "   AND jc.tournamentName = t.tournamentName " +
            " )" +
            " AND EXISTS( " +
            "   SELECT * FROM Puzzle pp" +
            "   INNER JOIN TournamentPuzzleJoin jp" +
            "   ON jp.puzzleId = pp.uniqueId " +
            "   WHERE jp.tournamentName = t.tournamentName " +
            "   AND NOT EXISTS( " +
            "       SELECT * FROM Game g" +
            "       WHERE pp.uniqueId = g.puzzleId " +
            "       AND g.playedBy = :username " +
            "   ) " +
            " ) " +
            " AND NOT EXISTS( " +
            "   SELECT * FROM TournamentPlayThrough tpt " +
            "   WHERE tpt.tournamentName = t.tournamentName " +
            "   AND tpt.playedBy = :username)" )
    List<Tournament> getAllTournamentsPlayerCanPlay(String username);
}
