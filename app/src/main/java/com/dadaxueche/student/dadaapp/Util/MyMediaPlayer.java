package com.dadaxueche.student.dadaapp.Util;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by wpf on 10-6-0006.
 */
public class MyMediaPlayer{

    public static MediaPlayer mediaPlayer;

    public MyMediaPlayer(MediaPlayer mediaPlayer) {
        MyMediaPlayer.mediaPlayer = mediaPlayer;
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void reset() {
        mediaPlayer.reset();
    }

    public void start() {
        mediaPlayer.start();
    }

    public static MyMediaPlayer create(Context context,int id) {
        return new MyMediaPlayer(MediaPlayer.create(context,id));
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}
