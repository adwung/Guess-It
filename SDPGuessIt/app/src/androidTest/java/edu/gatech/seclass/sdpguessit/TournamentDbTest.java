package edu.gatech.seclass.sdpguessit;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import edu.gatech.seclass.sdpguessit.data.Game;
import edu.gatech.seclass.sdpguessit.data.GameDao;
import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.PlayerDao;
import edu.gatech.seclass.sdpguessit.data.Puzzle;
import edu.gatech.seclass.sdpguessit.data.PuzzleDao;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;
import edu.gatech.seclass.sdpguessit.data.Tournament;
import edu.gatech.seclass.sdpguessit.data.TournamentDao;
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThroughDao;
import edu.gatech.seclass.sdpguessit.data.TournamentPuzzleJoin;
import edu.gatech.seclass.sdpguessit.data.TournamentPuzzleJoinDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TournamentDbTest {

    private SDPGuessItDatabase          db;
    private PuzzleDao                   puzzleDao;
    private PlayerDao                   playerDao;
    private GameDao gameDao;
    private TournamentDao               tournamentDao;
    private TournamentPuzzleJoinDao tournamentPuzzleJoinDao;
    private TournamentPlayThroughDao playThroughDao;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, SDPGuessItDatabase.class).build();
        puzzleDao               = db.puzzleDao();
        playerDao               = db.playerDao();
        gameDao                 = db.gameDao();
        tournamentDao           = db.tournamentDao();
        tournamentPuzzleJoinDao = db.tournamentPuzzleJoinDao();
        playThroughDao          = db.playThroughDao();
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

    private Puzzle createAndSavePuzzle(Player pl, String phrase) {
        Puzzle pz = new Puzzle();
        pz.setCreatedByUsername(pl.getUsername());
        pz.setMaxWrongGuesses(10);
        pz.setPhrase(phrase);
        puzzleDao.insert(pz);
        pz.setUniqueId(puzzleDao.getLastPuzzleId());
        return pz;
    }

    private Game createAndSaveGame(Player player, Puzzle puzzle) {
        Game game = new Game();
        game.setCompleted(false);
        game.setPuzzleId(puzzle.getUniqueId());
        game.setTotalPrize(0);
        game.setWrongGuesses(0);
        game.setDisplayPhrase("_______ ______");
        game.setPlayedBy(player.getUsername());
        gameDao.insert(game);
        return game;
    }

    private Tournament createAndSaveTournament(String tournamentName, Player creator) {
        Tournament tournament = new Tournament();
        tournament.setCreatedByUsername(creator.getUsername());
        tournament.setTournamentName(tournamentName);

        tournamentDao.insert(tournament);
        return tournament;
    }

    private void addAndSavePuzzleToTournament(Tournament tournament, Puzzle puzzle) {
        TournamentPuzzleJoin tournamentPuzzleJoin = new TournamentPuzzleJoin();
        tournamentPuzzleJoin.setTournamentName(tournament.getTournamentName());
        tournamentPuzzleJoin.setPuzzleId(puzzle.getUniqueId());
        tournamentPuzzleJoinDao.insert(tournamentPuzzleJoin);
    }


    @Test
    public void shouldCreateDB() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(tournamentDao);
    }

    @Test
    public void shouldInsertTournament() {
        Player player = createAndSavePlayer("testplayer");
        String tournamentName = "TestTournament";
        Tournament tournament = new Tournament();
        tournament.setCreatedByUsername(player.getUsername());
        tournament.setTournamentName(tournamentName);

        tournamentDao.insert(tournament);
        List<Tournament> tournaments = tournamentDao.getAllTournaments();

        assertEquals(1, tournaments.size());
        Tournament dbTournament = tournaments.get(0);
        assertEquals(player.getUsername(), dbTournament.getCreatedByUsername());
        assertEquals(tournamentName, dbTournament.getTournamentName());
    }



    @Test
    public void shouldGetTournamentByName() {
        Player player = createAndSavePlayer("testplayer");
        String tournamentName = "TestTournament";
        Tournament tournament = new Tournament();
        tournament.setCreatedByUsername(player.getUsername());
        tournament.setTournamentName(tournamentName);

        tournamentDao.insert(tournament);

        tournament = new Tournament();
        tournament.setCreatedByUsername(player.getUsername());
        tournament.setTournamentName("TestTournament2");

        tournamentDao.insert(tournament);

        tournament = new Tournament();
        tournament.setCreatedByUsername(player.getUsername());
        tournament.setTournamentName("TestTournament3");

        tournamentDao.insert(tournament);

        Tournament dbTournament = tournamentDao.getTournamentByName(tournamentName);
        assertEquals(player.getUsername(), dbTournament.getCreatedByUsername());
        assertEquals(tournamentName, dbTournament.getTournamentName());
    }




    @Test(expected = SQLiteConstraintException.class)
    public void shouldNotInsertWithBadCreatedBy() {
        String tournamentName = "TestTournament";
        Tournament tournament = new Tournament();
        tournament.setCreatedByUsername("bob");
        tournament.setTournamentName(tournamentName);

        tournamentDao.insert(tournament);
    }

    @Test
    public void shouldGetAllTournamentsPlayerCanPlay() {
        Player creator1 = createAndSavePlayer("bob123");
        Puzzle puzzle1 = createAndSavePuzzle(creator1, "Tim Was Here");
        Puzzle puzzle2 = createAndSavePuzzle(creator1, "Puzzle2");
        Player otherCreator = createAndSavePlayer("tim123");
        Puzzle puzzle3 = createAndSavePuzzle(otherCreator, "Puzzle3 Other");

        Tournament creator1Tournament = createAndSaveTournament("creator1Tournament", creator1);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle1);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle2);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle3);

        Player player = createAndSavePlayer("player");

        List<Tournament> canPlay = tournamentDao.getAllTournamentsPlayerCanPlay(creator1.getUsername());
        assertEquals(0, canPlay.size());

        canPlay = tournamentDao.getAllTournamentsPlayerCanPlay(otherCreator.getUsername());
        assertEquals(0, canPlay.size());

        canPlay = tournamentDao.getAllTournamentsPlayerCanPlay(player.getUsername());
        assertEquals(1, canPlay.size());

        createAndSaveGame(player, puzzle1);
        canPlay = tournamentDao.getAllTournamentsPlayerCanPlay(player.getUsername());
        assertEquals(1, canPlay.size());

        createAndSaveGame(player, puzzle2);
        canPlay = tournamentDao.getAllTournamentsPlayerCanPlay(player.getUsername());
        assertEquals(1, canPlay.size());

        createAndSaveGame(player, puzzle3);
        canPlay = tournamentDao.getAllTournamentsPlayerCanPlay(player.getUsername());
        assertEquals(0, canPlay.size());
    }
}
