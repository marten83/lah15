package se.martenolsson.lah15;

import android.app.Application;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.parse.Parse;

import se.martenolsson.lah15.classes.Foreground;

public class ApplicationController extends Application {

	public Typeface geoSans;
    public Typeface geoSansBold;
    public Typeface caviar;
    public Typeface icons;
    public MediaPlayer myMediaPlayer;
    public String currentSong = null;
    public String currentArtist = null;

	@Override
	public void onCreate() {
		super.onCreate();

        Foreground.init(this);

        myMediaPlayer = new MediaPlayer();
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		geoSans = Typeface.createFromAsset(this.getAssets(), "cardenio.ttf");
        geoSansBold = Typeface.createFromAsset(this.getAssets(), "cardeniobold.ttf");
        caviar = Typeface.createFromAsset(this.getAssets(), "lane.ttf");
        icons = Typeface.createFromAsset(this.getAssets(), "Flaticon.ttf");
		//Parse
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "VrCJhCigE2n6CAXcYilfkw4hsEuvNkiK6MVXfJAC", "D4ctM4xMjtScCmFSW7xMSBqv49Hw8YbMJslbnYO5");
		//Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
	}

}