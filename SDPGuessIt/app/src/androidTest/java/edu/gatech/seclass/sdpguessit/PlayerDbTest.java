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

import edu.gatech.seclass.sdpguessit.data.Player;
import edu.gatech.seclass.sdpguessit.data.PlayerDao;
import edu.gatech.seclass.sdpguessit.data.SDPGuessItDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class PlayerDbTest {


    private PlayerDao playerDao;
    private SDPGuessItDatabase db;


    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, SDPGuessItDatabase.class).build();
        playerDao = db.playerDao();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }


    @Test
    public void shouldCreateDB() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(playerDao);
    }

    @Test
    public void shouldInsertPlayer() {
        Player player = new Player();
        player.setUsername("username1");
        player.setFirstName("Bob");
        player.setLastName("Smith");
        player.setEmail("bsmith@email.com");
        playerDao.insert(player);
        List<Player> players = playerDao.getAllPlayers();

        assertEquals(1, players.size());
        Player dbPlayer = players.get(0);
        assertEquals(player.getUsername(), dbPlayer.getUsername());
        assertEquals(player.getEmail(), dbPlayer.getEmail());
        assertEquals(player.getFirstName(), dbPlayer.getFirstName());
        assertEquals(player.getLastName(), dbPlayer.getLastName());
    }
}
