package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert
    void insert(Player... players);

    @Update
    void update(Player... players);

    @Delete
    void delete(Player... players);

    @Query("SELECT * FROM player")
    List<Player> getAllPlayers();

    @Query("SELECT * FROM player WHERE username = :username")
    Player getPlayerByUsername(String username);
}
