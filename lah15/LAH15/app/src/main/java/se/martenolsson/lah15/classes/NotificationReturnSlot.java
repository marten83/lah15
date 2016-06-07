package se.martenolsson.lah15.classes;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import se.martenolsson.lah15.ApplicationController;
import se.martenolsson.lah15.MainActivity;

/**
 * Created by martenolsson on 16-04-23.
 */
public class NotificationReturnSlot extends Activity {

    MediaPlayer myMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myMediaPlayer = ((ApplicationController) getApplicationContext()).myMediaPlayer;

        String action = (String) getIntent().getExtras().get("DO");

        if (action.equals("volume")) {
            if(myMediaPlayer.getCurrentPosition() > 0 && !myMediaPlayer.isPlaying()){
                myMediaPlayer.start();
                new NotificationPanel(this, true);
            }else{
                myMediaPlayer.pause();
                new NotificationPanel(this, true);
            }
        }else if (action.equals("stop")) {
            if(myMediaPlayer.isPlaying()) {
                myMediaPlayer.stop();
            }
            NotificationPanel.notificationCancel();
        }
        else if (action.equals("message")) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(notificationIntent);
        }
        finish();
    }
}