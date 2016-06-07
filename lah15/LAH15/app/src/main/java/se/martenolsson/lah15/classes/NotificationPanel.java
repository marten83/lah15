package se.martenolsson.lah15.classes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import se.martenolsson.lah15.ApplicationController;
import se.martenolsson.lah15.R;

/**
 * Created by martenolsson on 16-04-23.
 */
public class NotificationPanel {

    private Context parent;
    private static NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;
    MediaPlayer myMediaPlayer;

    public NotificationPanel(Context parent, Boolean isForeground) {
        this.parent = parent;
        if(isForeground) {
            nBuilder = new NotificationCompat.Builder(parent)
                    .setContentTitle("")
                    .setSmallIcon(R.drawable.status_icon)
                    .setOngoing(true);

            remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationview);

            //set the button listeners
            setListeners(remoteView);
            nBuilder.setContent(remoteView);

            nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(2, nBuilder.build());
        }else{
            notificationCancel();
        }
    }

    public void setListeners(RemoteViews view){
        //listener 1
        Intent volume = new Intent(parent, NotificationReturnSlot.class);
        volume.putExtra("DO", "volume");
        PendingIntent btn1 = PendingIntent.getActivity(parent, 0, volume, 0);
        view.setOnClickPendingIntent(R.id.btn1, btn1);

        //listener 2
        Intent stop = new Intent(parent, NotificationReturnSlot.class);
        stop.putExtra("DO", "stop");
        PendingIntent btn2 = PendingIntent.getActivity(parent, 1, stop, 0);
        view.setOnClickPendingIntent(R.id.btn2, btn2);

        //listener 3
        Intent message = new Intent(parent, NotificationReturnSlot.class);
        message.putExtra("DO", "message");
        PendingIntent btn3 = PendingIntent.getActivity(parent, 2, message, 0);
        view.setOnClickPendingIntent(R.id.clickBox, btn3);

        String artist = ((ApplicationController) parent.getApplicationContext()).currentArtist;
        view.setTextViewText(R.id.message, artist);

        myMediaPlayer = ((ApplicationController) parent.getApplicationContext()).myMediaPlayer;

        if(myMediaPlayer.getCurrentPosition() > 0 && !myMediaPlayer.isPlaying()){
            //view.setTextViewText(R.id.notiPlay, "U");
            Bitmap largeIcon = BitmapFactory.decodeResource(parent.getResources(), R.drawable.play);
            view.setImageViewBitmap(R.id.notiPlay, largeIcon);
        }else{
            //view.setTextViewText(R.id.btn1, "P");
            Bitmap largeIcon = BitmapFactory.decodeResource(parent.getResources(), R.drawable.pause);
            view.setImageViewBitmap(R.id.notiPlay, largeIcon);
        }
    }

    public static void notificationCancel() {
        if(nManager != null) {
            nManager.cancel(2);
        }
    }
}