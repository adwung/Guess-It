package edu.gatech.seclass.sdpguessit;

import android.app.Application;

import com.facebook.stetho.Stetho;

import edu.gatech.seclass.sdpguessit.data.Player;

public class SDPGuessItApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
