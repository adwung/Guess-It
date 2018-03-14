package edu.gatech.seclass.sdpguessit.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(
        entities = {
                Player.class,
                Puzzle.class,
                Game.class,
                Tournament.class,
                TournamentPuzzleJoin.class,
                TournamentPlayThrough.class
        },
        version = 1
)
public abstract class SDPGuessItDatabase extends RoomDatabase {

    public static final String DB_NAME = "guess-it-database";

    private static SDPGuessItDatabase INSTANCE;

    public abstract PlayerDao playerDao();
    public abstract PuzzleDao puzzleDao();
    public abstract GameDao gameDao();
    public abstract TournamentDao tournamentDao();
    public abstract TournamentPuzzleJoinDao tournamentPuzzleJoinDao();
    public abstract TournamentPlayThroughDao playThroughDao();

    public static SDPGuessItDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), SDPGuessItDatabase.class, SDPGuessItDatabase.DB_NAME)
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(SDPGuessItDatabase.DB_NAME);
        SDPGuessItDatabase.destroyInstance();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
