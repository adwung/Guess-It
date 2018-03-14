package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TournamentPlayThroughDao {

    @Insert
    void insert(TournamentPlayThrough... playThroughs);

    @Update
    void update(TournamentPlayThrough... playThroughs);

    @Delete
    void delete(TournamentPlayThrough... playThroughs);

    @Query("SELECT * FROM TournamentPlayThrough")
    List<TournamentPlayThrough> getAllPlayThroughs();

    @Query("SELECT * FROM TournamentPlayThrough where completed = :completed")
    List<TournamentPlayThrough> getAllPlayThroughsByComplete(boolean completed);

    @Query("SELECT * FROM TournamentPlayThrough WHERE playedBy = :playedBy")
    List<TournamentPlayThrough> getAllPlayThroughsByPlayedBy(String playedBy);

    @Query("SELECT * FROM TournamentPlayThrough WHERE tournamentName = :tournamentName")
    List<TournamentPlayThrough> getAllPlayThroughsByTournament(String tournamentName);

    @Query("SELECT * FROM TournamentPlayThrough WHERE tournamentName = :tournamentName AND playedBy = :playedBy")
    TournamentPlayThrough getTournamentByTournamentAndPlayedBy(String tournamentName, String playedBy);

    @Query("SELECT * FROM TournamentPlayThrough " +
            " INNER JOIN TournamentPuzzleJoin " +
            " ON TournamentPuzzleJoin.tournamentName = TournamentPlayThrough.tournamentName " +
            " WHERE TournamentPlayThrough.playedBy = :playedBy " +
            " AND TournamentPuzzleJoin.puzzleId = :puzzleId")
    List<TournamentPlayThrough> getTournamentByTournamentAndPlayedByWithPuzzle(String playedBy, int puzzleId);

    @Query("SELECT * FROM Puzzle " +
            " INNER JOIN TournamentPuzzleJoin " +
            " ON TournamentPuzzleJoin.puzzleId = Puzzle.uniqueId " +
            " WHERE TournamentPuzzleJoin.tournamentName = :tournamentName")
    List<Puzzle> getPuzzlesForTournament(String tournamentName);

    @Query("SELECT * FROM Puzzle " +
            " INNER JOIN TournamentPuzzleJoin " +
            " ON TournamentPuzzleJoin.puzzleId = Puzzle.uniqueId " +
            " WHERE TournamentPuzzleJoin.tournamentName = :tournamentName " +
            " AND NOT EXISTS( " +
            " SELECT * FROM Game " +
            " WHERE Puzzle.uniqueId = Game.puzzleId " +
            " AND Game.playedBy = :playedBy )")
    List<Puzzle> getPuzzlesNotPlayedForPlayThrough(String tournamentName, String playedBy);

    @Query("SELECT * FROM Game " +
            " INNER JOIN Puzzle " +
            " ON Puzzle.uniqueId = Game.puzzleId " +
            " INNER JOIN TournamentPuzzleJoin " +
            " ON TournamentPuzzleJoin.puzzleId = Puzzle.uniqueId " +
            " WHERE TournamentPuzzleJoin.tournamentName = :tournamentName " +
            " AND Game.playedBy = :playedBy")
    List<Game> getGamesAlreadyPlayedForPlayThrough(String tournamentName, String playedBy);



    @Query("SELECT * FROM TournamentPlayThrough pt " +
            " WHERE EXISTS( " +
            "   SELECT * FROM Puzzle p " +
            "   INNER JOIN TournamentPuzzleJoin j " +
            "   ON j.puzzleId = p.uniqueId " +
            "   WHERE j.tournamentName = pt.tournamentName " +
            "   AND NOT EXISTS( " +
            "       SELECT * FROM Game g " +
            "       WHERE p.uniqueId = g.puzzleId " +
            "       AND g.playedBy = pt.playedBy " +
            "   )" +
            " )")
    List<TournamentPlayThrough> getAllIncompletePlayThroughs();



    @Query("SELECT * FROM TournamentPlayThrough pt " +
            " WHERE NOT EXISTS( " +
            "   SELECT * FROM Puzzle p " +
            "   INNER JOIN TournamentPuzzleJoin j " +
            "   ON j.puzzleId = p.uniqueId " +
            "   WHERE j.tournamentName = pt.tournamentName " +
            "   AND NOT EXISTS( " +
            "       SELECT * FROM Game g " +
            "       WHERE p.uniqueId = g.puzzleId " +
            "       AND g.playedBy = pt.playedBy " +
            "   )" +
            " )")
    List<TournamentPlayThrough> getAllCompletedPlayThroughs();


    @Query("SELECT * FROM TournamentPlayThrough pt " +
            " WHERE pt.playedBy = :username " +
            " AND NOT EXISTS( " +
            "   SELECT * FROM Puzzle p " +
            "   INNER JOIN TournamentPuzzleJoin j " +
            "   ON j.puzzleId = p.uniqueId " +
            "   WHERE j.tournamentName = pt.tournamentName " +
            "   AND NOT EXISTS( " +
            "       SELECT * FROM Game g " +
            "       WHERE p.uniqueId = g.puzzleId " +
            "       AND g.playedBy = pt.playedBy " +
            "   )" +
            " )")
    List<TournamentPlayThrough> getCompletedPlayThroughsForPlayer(String username);



    @Query("SELECT * FROM TournamentPlayThrough pt " +
            " WHERE pt.playedBy = :username " +
            " AND EXISTS( " +
            "   SELECT * FROM Puzzle p " +
            "   INNER JOIN TournamentPuzzleJoin j " +
            "   ON j.puzzleId = p.uniqueId " +
            "   WHERE j.tournamentName = pt.tournamentName " +
            "   AND NOT EXISTS( " +
            "       SELECT * FROM Game g " +
            "       WHERE p.uniqueId = g.puzzleId " +
            "       AND g.playedBy = pt.playedBy " +
            "   )" +
            " )")
    List<TournamentPlayThrough> getIncompletedPlayThroughsForPlayer(String username);


    @Query("SELECT * FROM TournamentPlayThrough WHERE tournamentName = :tournamentName " +
            " ORDER BY totalPrize DESC LIMIT 1")
    TournamentPlayThrough getTopScoringPlaythroughForTournament(String tournamentName);

    @Query("SELECT count(*) FROM TournamentPlayThrough WHERE tournamentName = :tournamentName")
    Integer getPlayerCountForTournament(String tournamentName);


    @Query("SELECT * FROM TournamentPlayThrough pt " +
            " WHERE tournamentName = :tournamentName " +
            " AND NOT EXISTS( " +
            "   SELECT * FROM Puzzle p " +
            "   INNER JOIN TournamentPuzzleJoin j " +
            "   ON j.puzzleId = p.uniqueId " +
            "   WHERE j.tournamentName = pt.tournamentName " +
            "   AND NOT EXISTS( " +
            "       SELECT * FROM Game g " +
            "       WHERE p.uniqueId = g.puzzleId " +
            "       AND g.playedBy = pt.playedBy " +
            "   )" +
            " ) " +
            " ORDER BY totalPrize DESC LIMIT 1")
    TournamentPlayThrough getTopScoringCompletedPlaythroughForTournament(String tournamentName);


    @Query("SELECT count(*) FROM TournamentPlayThrough pt " +
            " WHERE tournamentName = :tournamentName " +
            " AND NOT EXISTS( " +
            "   SELECT * FROM Puzzle p " +
            "   INNER JOIN TournamentPuzzleJoin j " +
            "   ON j.puzzleId = p.uniqueId " +
            "   WHERE j.tournamentName = pt.tournamentName " +
            "   AND NOT EXISTS( " +
            "       SELECT * FROM Game g " +
            "       WHERE p.uniqueId = g.puzzleId " +
            "       AND g.playedBy = pt.playedBy " +
            "   )" +
            " )")
    Integer getCountOfCompletedPlayThroughsForTournament(String tournamentName);
}
