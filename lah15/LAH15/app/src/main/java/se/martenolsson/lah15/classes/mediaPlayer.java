package se.martenolsson.lah15.classes;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import se.martenolsson.lah15.ApplicationController;
import se.martenolsson.lah15.playListView;

/**
 * Created by martenolsson on 16-04-22.
 */
public class mediaPlayer {
    MediaPlayer myMediaPlayer;
    TinyDB tinydb;
    ArrayList<String> playList = new ArrayList<>();
    String mp3;
    String mediaUrl;
    Boolean fromPlayBtn;
    Boolean fromSingle;
    LinearLayout vanta;
    LinearLayout stopBtn;
    LinearLayout playBtn;
    String artist;
    Context mContext;

    public mediaPlayer(Context mContext, String artist, String mp3, final Boolean fromPlayBtn, final LinearLayout vanta, final LinearLayout stopBtn, final LinearLayout playBtn, final Boolean fromSingle) {
        myMediaPlayer = ((ApplicationController) mContext.getApplicationContext()).myMediaPlayer;
        tinydb = new TinyDB(mContext);
        this.mContext = mContext;
        this.mp3 = mp3;
        this.artist = artist;
        mediaUrl = mp3.replace(" ","%20").replace("¦", "%C2%A6");

        this.fromPlayBtn = fromPlayBtn;
        this.fromSingle = fromSingle;
        this.vanta = vanta;
        this.stopBtn = stopBtn;
        this.playBtn = playBtn;

        if(fromSingle) {
            if (fromPlayBtn) {
                if (myMediaPlayer.getCurrentPosition() > 0) {
                    vanta.setVisibility(View.VISIBLE);
                    myMediaPlayer.stop();
                    myMediaPlayer.reset();
                    playSong();
                } else {
                    myMediaPlayer.start();
                    stopBtn.setVisibility(View.VISIBLE);
                }
            } else {
                if (myMediaPlayer.getCurrentPosition() > 0) {
                    vanta.setVisibility(View.GONE);
                    playBtn.setVisibility(View.VISIBLE);
                } else {
                    playSong();
                }
            }
        }else{
            if (myMediaPlayer.getCurrentPosition() > 0) {
                myMediaPlayer.stop();
                myMediaPlayer.reset();
                playSong();
            }else{
                playSong();
            }
        }
    }

    void playSong(){
        try {
            myMediaPlayer.setDataSource(mediaUrl);
            myMediaPlayer.prepareAsync();

        } catch (IOException e) {
            Toast.makeText(mContext, "Kan inte spela låt", Toast.LENGTH_LONG).show();
            playBtn.setVisibility(View.GONE);
            vanta.setVisibility(View.GONE);
            e.printStackTrace();
        }

        //mp3 will be started after completion of preparing...
        myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {

                if(fromPlayBtn){
                    playList = tinydb.getList("playList");

                    for (int i = 0; i < playList.size(); i++) {
                        if (playList.get(i).contains("fromPlay")) {
                            playList.remove(i);
                        }
                    }
                    for (int i = 0; i < playList.size(); i++) {
                        if (playList.get(i).contains(artist)) {
                            playList.remove(i);
                        }
                    }


                    playList.add(0, artist + ";;" + mp3 + ";;" + "fromPlay");

                    tinydb.putList("playList", playList);
                }

                ((ApplicationController) mContext.getApplicationContext()).currentSong = mp3;
                ((ApplicationController) mContext.getApplicationContext()).currentArtist = artist;

                if (fromSingle) {
                    if (fromPlayBtn) {
                        vanta.setVisibility(View.GONE);
                        stopBtn.setVisibility(View.VISIBLE);
                        myMediaPlayer.start();
                    } else {
                        vanta.setVisibility(View.GONE);
                        playBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    myMediaPlayer.start();
                    if(Foreground.get().isBackground()){
                        new NotificationPanel(mContext, true);
                    }
                }

                if(playListView.adapter != null) {
                    playListView.adapter.notifyDataSetChanged();
                }
            }

        });

        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp.getCurrentPosition() != 0) {
                    playList = tinydb.getList("playList");
                    if (playList.size() > 0) {
                        int index = 0;
                        for (int i = 0; i < playList.size(); i++) {
                            if (playList.get(i).contains(mp3)) {
                                index = i + 1;
                            }
                        }

                        if (playList.size() > index) {
                            String[] listitem = playList.get(index).split(";;");
                            artist = listitem[0];
                            mp3 = listitem[1];

                            mediaUrl = mp3.replace(" ", "%20").replace("¦", "%C2%A6");
                            mp.stop();
                            mp.reset();
                            playSong();
                        }
                    }
                }
            }
        });

        myMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(final MediaPlayer mp, final int what, final int extra) {
                if (what != -38) {
                    if (fromSingle) {
                        playBtn.setVisibility(View.GONE);
                        vanta.setVisibility(View.GONE);
                    }
                    Toast.makeText(mContext, "Kan inte spela låt", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

    }
}
