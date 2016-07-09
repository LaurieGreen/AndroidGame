package com.example.alyn.alphabuild;

/**
 * Created by Alyn on 30/06/16.
 */

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

public class SoundManager implements SoundPool.OnLoadCompleteListener {
    private SoundPool soundPool;
    boolean soundisloaded = false;
    int start = -1;
    int win = -1;
    int bump = -1;
    int destroyed = -1;
    int boost = -1;
    int boostID;

    public void loadSound(Context context) {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);
        start = soundPool.load(context, R.raw.start, 1);
        destroyed = soundPool.load(context, R.raw.destroyed, 1);
        win = soundPool.load(context, R.raw.win, 1);
        bump = soundPool.load(context, R.raw.bump, 1);
        boost = soundPool.load(context, R.raw.engine, 1);
    }
    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int status)
    {
        soundisloaded=true;
    }

    public void playWin()
    {
        soundPool.play(win, 1, 1, 1, 0, 1f);
    }
    public void playStart()
    {
        soundPool.play(start, 1, 1, 1, 0, 1f);
    }
    public void playDestroyed()
    {
        soundPool.play(destroyed, 1, 1, 1, 0, 1f);
    }
    public void playBump()
    {
        soundPool.play(bump, 1, 1, 1, 0, 1f);
    }
    public void playBoost()
    {
        boostID = soundPool.play(boost, 1, 1, 1, -1, 1f);
    }
    public void setVolumeBoost(float volume)
    {
        soundPool.setVolume(boostID, volume, volume);
    }

}// End SoundManager
