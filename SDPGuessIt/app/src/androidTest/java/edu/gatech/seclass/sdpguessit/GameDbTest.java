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

import java.util.ArrayList;
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

@RunWith(AndroidJUnit4.class)
public class GameDbTest {


    private PlayerDao playerDao;
    private PuzzleDao puzzleDao;
    private GameDao gameDao;
    private SDPGuessItDatabase db;


    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, SDPGuessItDatabase.class).build();
        playerDao = db.playerDao();
        puzzleDao = db.puzzleDao();
        gameDao = db.gameDao();
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


    @Test
    public void shouldCreateDB() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(gameDao);
    }

    @Test
    public void shouldInsertPlayer() {
        Player creator = createAndSavePlayer("creator");
        Player player = createAndSavePlayer("player");
        Puzzle puzzle = createAndSavePuzzle(creator, "creator puzzle");
        Game game = new Game();
        game.setCompleted(false);
        game.setPuzzleId(puzzle.getUniqueId());
        game.setTotalPrize(0);
        game.setWrongGuesses(0);
        game.setDisplayPhrase("_______ ______");
        game.setPlayedBy(player.getUsername());
        gameDao.insert(game);
        List<Game> games = gameDao.getAllGames();

        assertEquals(1, games.size());
        Game dbGame = games.get(0);
        assertEquals(game.getDisplayPhrase(), dbGame.getDisplayPhrase());
        assertEquals(game.getPlayedBy(), dbGame.getPlayedBy());
        assertEquals(game.getPuzzleId(), dbGame.getPuzzleId());
        assertEquals(game.getTotalPrize(), dbGame.getTotalPrize());
        assertEquals(game.getWrongGuesses(), dbGame.getWrongGuesses());
    }

    @Test(expected = SQLiteConstraintException.class)
    public void shouldNotInsertWithBadPlayedBy() {
        Player creator = createAndSavePlayer("creator");
        Puzzle puzzle = createAndSavePuzzle(creator, "creator puzzle");
        Game game = new Game();
        game.setCompleted(false);
        game.setPuzzleId(puzzle.getUniqueId());
        game.setTotalPrize(0);
        game.setWrongGuesses(0);
        game.setDisplayPhrase("_______ ______");
        game.setPlayedBy("player");
        gameDao.insert(game);
    }


    @Test(expected = SQLiteConstraintException.class)
    public void shouldNotInsertWithBadPuzzleId() {
        Player player = createAndSavePlayer("player");
        Game game = new Game();
        game.setCompleted(false);
        game.setPuzzleId(1);
        game.setTotalPrize(0);
        game.setWrongGuesses(0);
        game.setDisplayPhrase("_______ ______");
        game.setPlayedBy(player.getUsername());
        gameDao.insert(game);
    }


    @Test
    public void shouldNotSaveLetters() {
        Player creator = createAndSavePlayer("creator");
        Player player = createAndSavePlayer("player");
        Puzzle puzzle = createAndSavePuzzle(creator, "creator puzzle");
        Game game = new Game();
        game.setCompleted(false);
        game.setPuzzleId(puzzle.getUniqueId());
        game.setTotalPrize(0);
        game.setWrongGuesses(0);
        game.setDisplayPhrase("_______ ______");
        game.setPlayedBy(player.getUsername());
        List<Character> chars = new ArrayList<Character>();
        chars.add('a');
        game.setUnquessedLetters(chars);

        //Verify we set the letters
        assertEquals(1, game.getUnquessedLetters().size());

        gameDao.insert(game);
        Game dbGame = gameDao.getGameByPayerAndPuzzle(player.getUsername(), puzzle.getUniqueId());
        List<Character> dbChars = dbGame.getUnquessedLetters();

        //Verify that the list of letters was not saved
        assertEquals(null, dbChars);
    }


    @Test
    public void shouldGetTopScoringGameForPuzzle() {
        Player creator = createAndSavePlayer("creator");
        Player player1 = createAndSavePlayer("player1");
        Player playerTop = createAndSavePlayer("playerTop");
        Puzzle puzzle = createAndSavePuzzle(creator, "creator puzzle");

        Game game = new Game();
        game.setCompleted(true);
        game.setPuzzleId(puzzle.getUniqueId());
        game.setTotalPrize(0);
        game.setWrongGuesses(0);
        game.setDisplayPhrase("_______ ______");
        game.setPlayedBy(player1.getUsername());
        gameDao.insert(game);

        game.setTotalPrize(1000);
        game.setPlayedBy(playerTop.getUsername());
        gameDao.insert(game);

        Game gameTop = gameDao.getTopScoringGameForPuzzle(puzzle.getUniqueId());

        assertEquals(game.getPlayedBy(), gameTop.getPlayedBy());
        assertEquals(game.getPuzzleId(), gameTop.getPuzzleId());
        assertEquals(game.getTotalPrize(), gameTop.getTotalPrize());
        assertEquals(game.getWrongGuesses(), gameTop.getWrongGuesses());
    }

    @Test
    public void shouldGetTopScoringGameForPuzzle_lessGuesses() {
        Player creator = createAndSavePlayer("creator");
        Player player1 = createAndSavePlayer("player1");
        Player player2 = createAndSavePlayer("player2");
        Player playerTop = createAndSavePlayer("playerTop");
        Puzzle puzzle = createAndSavePuzzle(creator, "creator puzzle");

        Game game = new Game();
        game.setCompleted(true);
        game.setPuzzleId(puzzle.getUniqueId());
        game.setTotalPrize(0);
        game.setWrongGuesses(0);
        game.setDisplayPhrase("_______ ______");
        game.setPlayedBy(player1.getUsername());
        gameDao.insert(game);

        game.setTotalPrize(1000);
        game.setPlayedBy(player2.getUsername());
        game.setWrongGuesses(8);
        gameDao.insert(game);

        game.setPlayedBy(playerTop.getUsername());
        game.setWrongGuesses(7);
        gameDao.insert(game);

        Game gameTop = gameDao.getTopScoringGameForPuzzle(puzzle.getUniqueId());

        assertEquals(game.getPlayedBy(), gameTop.getPlayedBy());
        assertEquals(game.getPuzzleId(), gameTop.getPuzzleId());
        assertEquals(game.getTotalPrize(), gameTop.getTotalPrize());
        assertEquals(game.getWrongGuesses(), gameTop.getWrongGuesses());
    }



    @Test
    public void shouldGetPlayerCountForPuzzle() {
        Player creator = createAndSavePlayer("creator");
        Player player1 = createAndSavePlayer("player1");
        Player playerTop = createAndSavePlayer("playerTop");
        Puzzle puzzle = createAndSavePuzzle(creator, "creator puzzle");

        Game game = new Game();
        game.setCompleted(true);
        game.setPuzzleId(puzzle.getUniqueId());
        game.setTotalPrize(0);
        game.setWrongGuesses(0);
        game.setDisplayPhrase("_______ ______");
        game.setPlayedBy(player1.getUsername());
        gameDao.insert(game);

        game.setTotalPrize(1000);
        game.setPlayedBy(playerTop.getUsername());
        gameDao.insert(game);

        Integer players = gameDao.getPlayerCountForPuzzle(puzzle.getUniqueId());

        assertEquals(players.intValue(), 2);
    }
}
