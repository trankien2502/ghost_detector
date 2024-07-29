package com.ghostdetctor.ghost_detector.ui.challenge.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ghostdetctor.ghost_detector.util.SPUtils;
import com.ghostdetector.ghost_detector.R;

public class SoundService extends Service {
    private MediaPlayer mediaPlayer;
    private int lastPosition;
    //private boolean isFirstStart = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.background_sound); // Replace with your sound file
        mediaPlayer.setLooping(true); // Loop the sound
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (!mediaPlayer.isPlaying()) {
                lastPosition = SPUtils.getInt(getBaseContext(),SPUtils.SOUND_POSITION,0);
                mediaPlayer.seekTo(lastPosition);
                Log.d("challengeCheck","position start: "+ lastPosition);
                mediaPlayer.start();
            } else if ("STOP_SOUND".equals(action)) {
                if (mediaPlayer.isPlaying()) {
                    SPUtils.setInt(getBaseContext(),SPUtils.SOUND_POSITION,mediaPlayer.getCurrentPosition());
                    Log.d("challengeCheck","position end: "+ mediaPlayer.getCurrentPosition());
                    mediaPlayer.pause();
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // Stop the service when the app is removed from recent tasks
        stopSelf();
    }
}
