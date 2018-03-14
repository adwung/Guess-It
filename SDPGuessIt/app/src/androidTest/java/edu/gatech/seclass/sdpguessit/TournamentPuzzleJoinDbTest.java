package edu.gatech.seclass.sdpguessit;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import edu.gatech.seclass.sdpguessit.data.GameDao;
import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.PlayerDao;
import edu.gatech.seclass.sdpguessit.data.Puzzle;
import edu.gatech.seclass.sdpguessit.data.PuzzleDao;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;
import edu.gatech.seclass.sdpguessit.data.Tournament;
import edu.gatech.seclass.sdpguessit.data.TournamentDao;
import edu.gatech.seclass.sdpguessit.data.TournamentPuzzleJoin;
import edu.gatech.seclass.sdpguessit.data.TournamentPuzzleJoinDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TournamentPuzzleJoinDbTest {


    private PlayerDao               playerDao;
    private PuzzleDao               puzzleDao;
    private TournamentDao           tournamentDao;
    private TournamentPuzzleJoinDao tournamentPuzzleJoinDao;
    private SDPGuessItDatabase      db;


    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, SDPGuessItDatabase.class).build();

        playerDao               = db.playerDao();
        puzzleDao               = db.puzzleDao();
        tournamentDao           = db.tournamentDao();
        tournamentPuzzleJoinDao = db.tournamentPuzzleJoinDao();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    private Player createAndSavePlayer(String username) {
        Player p = new Player();
        p.setUsername(username);
        p.setFirstName("abc");
        p.setLastName("123");
        p.setEmail("abc123@nope.nope");
        playerDao.insert(p);
        return p;
    }

    private Tournament createAndSaveTournament(Player pl, String name) {
        Tournament t = new Tournament();
        t.setTournamentName(name);
        t.setCreatedByUsername(pl.getUsername());
        tournamentDao.insert(t);
        return t;
    }

    private Puzzle createAndSavePuzzle(Player pl, String phrase) {
        Puzzle pz = new Puzzle();
        pz.setCreatedByUsername(pl.getUsername());
        pz.setMaxWrongGuesses(10);
        pz.setPhrase(phrase);
        puzzleDao.insert(pz);
        pz.setUniqueId(puzzleDao.getLastPuzzleId());
        return pz;
    }


    @Test
    public void shouldCreateDB() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(tournamentPuzzleJoinDao);
    }

    @Test
    public void shouldInsertTournament() {
        Player player = createAndSavePlayer("creator");
        Puzzle puzzle = createAndSavePuzzle(player, "Solve Me");
        Tournament tournament = createAndSaveTournament(player, "GreatTournament");

        TournamentPuzzleJoin tpj = new TournamentPuzzleJoin();
        tpj.setPuzzleId(puzzle.getUniqueId());
        tpj.setTournamentName(tournament.getTournamentName());

        tournamentPuzzleJoinDao.insert(tpj);

        List<Puzzle> pForTournament = tournamentPuzzleJoinDao.getPuzzlesForTournament(tournament.getTournamentName());
        assertEquals(1, pForTournament.size());
        Puzzle dbPuzzle = pForTournament.get(0);
        assertEquals(player.getUsername(), dbPuzzle.getCreatedByUsername());
        assertEquals(puzzle.getPhrase(), dbPuzzle.getPhrase());

        List<Tournament> tForPuzzle = tournamentPuzzleJoinDao.getTournamentsForPuzzle(puzzle.getUniqueId());
        assertEquals(1, tForPuzzle.size());
        Tournament dbTournament = tForPuzzle.get(0);
        assertEquals(player.getUsername(), dbTournament.getCreatedByUsername());
        assertEquals(tournament.getTournamentName(), dbTournament.getTournamentName());
    }
}
