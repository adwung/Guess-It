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
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThrough;
import edu.gatech.seclass.sdpguessit.data.TournamentPlayThroughDao;
import edu.gatech.seclass.sdpguessit.data.TournamentPuzzleJoin;
import edu.gatech.seclass.sdpguessit.data.TournamentPuzzleJoinDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TournamentPlayThroughDbTest {

    private SDPGuessItDatabase          db;
    private PuzzleDao                   puzzleDao;
    private PlayerDao                   playerDao;
    private GameDao                     gameDao;
    private TournamentDao               tournamentDao;
    private TournamentPuzzleJoinDao     tournamentPuzzleJoinDao;
    private TournamentPlayThroughDao    playThroughDao;

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

    private TournamentPlayThrough createAndSavePlayThrough(String tournamentName, Player player) {
        TournamentPlayThrough playThrough = new TournamentPlayThrough();
        playThrough.setTournamentName(tournamentName);
        playThrough.setPlayedBy(player.getUsername());
        playThrough.setCompleted(false);
        playThrough.setTotalPrize(42);
        playThroughDao.insert(playThrough);
        return playThrough;
    }


    @Test
    public void shouldCreateDB() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(playThroughDao);
    }

    @Test
    public void shouldInsertPlayThrough() {
        Player creator = createAndSavePlayer("creator");
        Player player = createAndSavePlayer("player");
        Tournament tournament = createAndSaveTournament("Test", creator);

        TournamentPlayThrough playThrough = new TournamentPlayThrough();
        playThrough.setTournamentName(tournament.getTournamentName());
        playThrough.setPlayedBy(player.getUsername());
        playThrough.setCompleted(false);
        playThrough.setTotalPrize(42);
        playThroughDao.insert(playThrough);

        List<TournamentPlayThrough> playThroughs = playThroughDao.getAllPlayThroughs();

        assertEquals(1, playThroughs.size());
        TournamentPlayThrough playThroughDb = playThroughs.get(0);
        assertEquals(playThrough.getPlayedBy(), playThroughDb.getPlayedBy());
        assertEquals(playThrough.getTotalPrize(), playThroughDb.getTotalPrize());
        assertEquals(playThrough.getTournamentName(), playThroughDb.getTournamentName());
        assertEquals(playThrough.isCompleted(), playThroughDb.isCompleted());
    }

    @Test(expected = SQLiteConstraintException.class)
    public void shouldNotInsertWithBadTournament() {
        Player player = createAndSavePlayer("player");
        TournamentPlayThrough playThrough = new TournamentPlayThrough();
        playThrough.setTournamentName("bad name");
        playThrough.setPlayedBy(player.getUsername());
        playThrough.setCompleted(false);
        playThrough.setTotalPrize(42);
        playThroughDao.insert(playThrough);
    }


    @Test(expected = SQLiteConstraintException.class)
    public void shouldNotInsertWithBadPlayer() {
        Player creator = createAndSavePlayer("creator");
        Tournament tournament = createAndSaveTournament("Test", creator);

        TournamentPlayThrough playThrough = new TournamentPlayThrough();
        playThrough.setTournamentName(tournament.getTournamentName());
        playThrough.setPlayedBy("bad player");
        playThrough.setCompleted(false);
        playThrough.setTotalPrize(42);
        playThroughDao.insert(playThrough);
    }

    @Test
    public void shouldGetGamesAlreadyPlayedForPlayThrough() {
        Player creator1 = createAndSavePlayer("bob123");
        Puzzle puzzle1 = createAndSavePuzzle(creator1, "Tim Was Here");
        Puzzle puzzle2 = createAndSavePuzzle(creator1, "Puzzle2");
        Player otherCreator = createAndSavePlayer("tim123");
        Puzzle puzzle3 = createAndSavePuzzle(otherCreator, "Puzzle3 Other");

        Game gamePuzzle3Creator1 = createAndSaveGame(creator1, puzzle3);

        Tournament creator1Tournament = createAndSaveTournament("creator1Tournament", creator1);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle1);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle2);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle3);

        Player player = createAndSavePlayer("player");
        TournamentPlayThrough playThrough = createAndSavePlayThrough(creator1Tournament.getTournamentName(), player);

        List<Game> played = playThroughDao.getGamesAlreadyPlayedForPlayThrough(creator1Tournament.getTournamentName(), player.getUsername());

        assertEquals(0, played.size());

        Game game1 = createAndSaveGame(player, puzzle1);

        played = playThroughDao.getGamesAlreadyPlayedForPlayThrough(creator1Tournament.getTournamentName(), player.getUsername());

        assertEquals(1, played.size());
        Game dbGame = played.get(0);
        assertEquals(game1.getDisplayPhrase(), dbGame.getDisplayPhrase());
        assertEquals(game1.getPlayedBy(), dbGame.getPlayedBy());
        assertEquals(game1.getPuzzleId(), dbGame.getPuzzleId());
        assertEquals(game1.getTotalPrize(), dbGame.getTotalPrize());
        assertEquals(game1.getWrongGuesses(), dbGame.getWrongGuesses());

        Game game2 = createAndSaveGame(player, puzzle2);

        played = playThroughDao.getGamesAlreadyPlayedForPlayThrough(creator1Tournament.getTournamentName(), player.getUsername());

        assertEquals(2, played.size());

        Game game3 = createAndSaveGame(player, puzzle3);

        played = playThroughDao.getGamesAlreadyPlayedForPlayThrough(creator1Tournament.getTournamentName(), player.getUsername());

        assertEquals(3, played.size());
    }



    @Test
    public void shouldGetPuzzlesNotPlayedForPlayThrough() {
        Player creator1 = createAndSavePlayer("bob123");
        Puzzle puzzle1 = createAndSavePuzzle(creator1, "Tim Was Here");
        Puzzle puzzle2 = createAndSavePuzzle(creator1, "Puzzle2");
        Player otherCreator = createAndSavePlayer("tim123");
        Puzzle puzzle3 = createAndSavePuzzle(otherCreator, "Puzzle3 Other");

        Game gamePuzzle3Creator1 = createAndSaveGame(creator1, puzzle3);

        Tournament creator1Tournament = createAndSaveTournament("creator1Tournament", creator1);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle1);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle2);
        addAndSavePuzzleToTournament(creator1Tournament, puzzle3);

        Player player = createAndSavePlayer("player");
        TournamentPlayThrough playThrough = createAndSavePlayThrough(creator1Tournament.getTournamentName(), player);

        List<Puzzle> unplayed = playThroughDao.getPuzzlesNotPlayedForPlayThrough(creator1Tournament.getTournamentName(), player.getUsername());

        assertEquals(3, unplayed.size());

        Game game1 = createAndSaveGame(player, puzzle1);

        unplayed = playThroughDao.getPuzzlesNotPlayedForPlayThrough(creator1Tournament.getTournamentName(), player.getUsername());

        assertEquals(2, unplayed.size());

        Game game2 = createAndSaveGame(player, puzzle2);

        unplayed = playThroughDao.getPuzzlesNotPlayedForPlayThrough(creator1Tournament.getTournamentName(), player.getUsername());

        assertEquals(1, unplayed.size());
        Puzzle dbPuzzle = unplayed.get(0);
        assertEquals(puzzle3.getUniqueId(), dbPuzzle.getUniqueId());
        assertEquals(puzzle3.getCreatedByUsername(), dbPuzzle.getCreatedByUsername());
        assertEquals(puzzle3.getMaxWrongGuesses(), dbPuzzle.getMaxWrongGuesses());
        assertEquals(puzzle3.getPhrase(), dbPuzzle.getPhrase());

        Game game3 = createAndSaveGame(player, puzzle3);

        unplayed = playThroughDao.getPuzzlesNotPlayedForPlayThrough(creator1Tournament.getTournamentName(), player.getUsername());

        assertEquals(0, unplayed.size());
    }




}


