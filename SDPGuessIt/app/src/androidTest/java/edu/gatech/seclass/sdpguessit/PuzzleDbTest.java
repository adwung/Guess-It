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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PuzzleDbTest {

    private PuzzleDao   puzzleDao;
    private PlayerDao   playerDao;
    private GameDao     gameDao;
    private SDPGuessItDatabase db;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, SDPGuessItDatabase.class).build();
        puzzleDao   = db.puzzleDao();
        playerDao   = db.playerDao();
        gameDao     = db.gameDao();

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


    @Test
    public void shouldCreateDB() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(puzzleDao);
    }

    @Test
    public void shouldInsertPuzzleWithAutoInc() {
        Player player = createAndSavePlayer("bob123");
        Puzzle puzzle = new Puzzle();
        puzzle.setMaxWrongGuesses(10);
        puzzle.setPhrase("Tim Was Here");
        puzzle.setCreatedByUsername(player.getUsername());
        puzzleDao.insert(puzzle);
        List<Puzzle> puzzles = puzzleDao.getAllPuzzles();

        assertEquals(1, puzzles.size());
        Puzzle dbPuzzle = puzzles.get(0);
        assertEquals(1, dbPuzzle.getUniqueId());
        assertEquals(puzzle.getCreatedByUsername(), dbPuzzle.getCreatedByUsername());
        assertEquals(puzzle.getMaxWrongGuesses(), dbPuzzle.getMaxWrongGuesses());
        assertEquals(puzzle.getPhrase(), dbPuzzle.getPhrase());
    }

    @Test
    public void shouldAutoInc() {
        Player player = createAndSavePlayer("bob123");
        Puzzle puzzle = new Puzzle();
        puzzle.setCreatedByUsername(player.getUsername());
        puzzle.setMaxWrongGuesses(10);
        puzzle.setPhrase("Tim was here");
        puzzleDao.insert(puzzle);
        puzzleDao.insert(puzzle);
        puzzleDao.insert(puzzle);

        List<Puzzle> puzzles = puzzleDao.getAllPuzzles();
        assertEquals(3, puzzles.size());
        Puzzle dbPuzzle = puzzles.get(2);
        assertEquals(3, dbPuzzle.getUniqueId());

    }

    @Test
    public void shouldAllowSettingUniqueId() {
        Player player = createAndSavePlayer("bob123");
        Puzzle puzzle = new Puzzle();
        puzzle.setMaxWrongGuesses(10);
        puzzle.setPhrase("Tim Was Here");
        puzzle.setCreatedByUsername(player.getUsername());
        puzzle.setUniqueId(10);
        puzzleDao.insert(puzzle);

        Puzzle dbPuzzle = puzzleDao.getPuzzleByUniqueId(10);
        assertEquals(puzzle.getPhrase(), dbPuzzle.getPhrase());
    }


    @Test(expected = SQLiteConstraintException.class)
    public void shouldNotInsertWithBadCreatedBy() {
        Puzzle puzzle = new Puzzle();
        puzzle.setMaxWrongGuesses(10);
        puzzle.setPhrase("Tim Was Here");
        puzzle.setCreatedByUsername("bob");
        puzzleDao.insert(puzzle);
    }

    @Test
    public void shouldGetPuzzlesCreatedBy() {
        Player creator = createAndSavePlayer("bob123");
        Puzzle puzzle1 = createAndSavePuzzle(creator, "Tim Was Here");
        Puzzle puzzle2 = createAndSavePuzzle(creator, "Puzzle2");
        Player otherCreator = createAndSavePlayer("tim123");
        Puzzle puzzle3 = createAndSavePuzzle(otherCreator, "Puzzle3 Other");

        List<Puzzle> puzzles = puzzleDao.getPuzzlesByCreatedBy(creator.getUsername());
        assertEquals(2, puzzles.size());

        puzzles = puzzleDao.getPuzzlesByCreatedBy(otherCreator.getUsername());
        assertEquals(1, puzzles.size());
    }


    @Test
    public void shouldGetPuzzlesPlayedBy() {
        Player creator = createAndSavePlayer("bob123");
        Puzzle puzzle1 = createAndSavePuzzle(creator, "Tim Was Here");
        Puzzle puzzle2 = createAndSavePuzzle(creator, "Puzzle2");
        Player otherCreator = createAndSavePlayer("tim123");
        Puzzle puzzle3 = createAndSavePuzzle(otherCreator, "Puzzle3 Other");

        Player player = createAndSavePlayer("player");
        Game game1 = createAndSaveGame(player, puzzle1);
        Game game2 = createAndSaveGame(player, puzzle3);
        Game gameOtherPlayer = createAndSaveGame(otherCreator, puzzle2);

        List<Puzzle> puzzles = puzzleDao.getPuzzlesByGamesPlayedBy(player.getUsername());
        assertEquals(2, puzzles.size());

        puzzles = puzzleDao.getPuzzlesByGamesPlayedBy(otherCreator.getUsername());
        assertEquals(1, puzzles.size());

        puzzles = puzzleDao.getPuzzlesByGamesPlayedBy(creator.getUsername());
        assertEquals(0, puzzles.size());
    }




    @Test
    public void shouldGetPuzzlesCreatedOrPlayedBy() {
        Player creator = createAndSavePlayer("bob123");
        Puzzle puzzle1 = createAndSavePuzzle(creator, "Tim Was Here");
        Puzzle puzzle2 = createAndSavePuzzle(creator, "Puzzle2");
        Player otherCreator = createAndSavePlayer("tim123");
        Puzzle puzzle3 = createAndSavePuzzle(otherCreator, "Puzzle3 Other");

        Player player = createAndSavePlayer("player");
        Game game1 = createAndSaveGame(player, puzzle1);
        Game game2 = createAndSaveGame(player, puzzle3);
        Game gameOtherPlayer = createAndSaveGame(otherCreator, puzzle2);


        //Only
        List<Puzzle> puzzles = puzzleDao.getPuzzlesCreatedOrPlayedBy(player.getUsername());
        assertEquals(2, puzzles.size());

        puzzles = puzzleDao.getPuzzlesCreatedOrPlayedBy(otherCreator.getUsername());
        assertEquals(2, puzzles.size());

        puzzles = puzzleDao.getPuzzlesCreatedOrPlayedBy(creator.getUsername());
        assertEquals(2, puzzles.size());
    }


    @Test
    public void shouldGetPuzzleIdsCreatedOrPlayedBy() {
        Player creator = createAndSavePlayer("bob123");
        Puzzle puzzle1 = createAndSavePuzzle(creator, "Tim Was Here");
        Puzzle puzzle2 = createAndSavePuzzle(creator, "Puzzle2");
        Player otherCreator = createAndSavePlayer("tim123");
        Puzzle puzzle3 = createAndSavePuzzle(otherCreator, "Puzzle3 Other");

        Player player = createAndSavePlayer("player");
        Game game1 = createAndSaveGame(player, puzzle1);
        Game game2 = createAndSaveGame(player, puzzle3);
        Game gameOtherPlayer = createAndSaveGame(otherCreator, puzzle2);


        //Only
        List<Integer> puzzles = puzzleDao.getPuzzleIdsCreatedOrPlayedBy(player.getUsername());
        assertEquals(2, puzzles.size());
        assertTrue(puzzles.contains(puzzle3.getUniqueId()));
        assertTrue(puzzles.contains(puzzle1.getUniqueId()));

        puzzles = puzzleDao.getPuzzleIdsCreatedOrPlayedBy(otherCreator.getUsername());
        assertEquals(2, puzzles.size());
        assertTrue(puzzles.contains(puzzle3.getUniqueId()));
        assertTrue(puzzles.contains(puzzle2.getUniqueId()));

        puzzles = puzzleDao.getPuzzleIdsCreatedOrPlayedBy(creator.getUsername());
        assertEquals(2, puzzles.size());
        assertTrue(puzzles.contains(puzzle1.getUniqueId()));
        assertTrue(puzzles.contains(puzzle2.getUniqueId()));
    }




    @Test
    public void shouldGetPuzzleIdsNotCreatedAndNotPlayedBy() {
        Player creator = createAndSavePlayer("bob123");
        Puzzle puzzle1 = createAndSavePuzzle(creator, "Tim Was Here");
        Puzzle puzzle2 = createAndSavePuzzle(creator, "Puzzle2");
        Player otherCreator = createAndSavePlayer("tim123");
        Puzzle puzzle3 = createAndSavePuzzle(otherCreator, "Puzzle3 Other");

        Player player = createAndSavePlayer("player");
        Game game1 = createAndSaveGame(player, puzzle1);
        Game game2 = createAndSaveGame(player, puzzle3);
        Game gameOtherPlayer = createAndSaveGame(otherCreator, puzzle2);


        List<Integer> puzzles = puzzleDao.getPuzzleIdsNotCreatedAndNotPlayedBy(otherCreator.getUsername());
        assertEquals(1, puzzles.size());
        assertTrue(puzzles.contains(puzzle1.getUniqueId()));

        puzzles = puzzleDao.getPuzzleIdsNotCreatedAndNotPlayedBy(player.getUsername());
        assertEquals(1, puzzles.size());
        assertTrue(puzzles.contains(puzzle2.getUniqueId()));

        puzzles = puzzleDao.getPuzzleIdsNotCreatedAndNotPlayedBy(creator.getUsername());
        assertEquals(1, puzzles.size());
        assertTrue(puzzles.contains(puzzle3.getUniqueId()));
    }


}
