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

public class SoundManager {
    private SoundPool soundPool;

    int start = -1;
    int win = -1;
    int bump = -1;
    int destroyed = -1;
    int boost = -1;
    int streamid = 0;

    public void loadSound(Context context) {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        //Create objects of the 2 required classes
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor descriptor;

        start = soundPool.load(context, R.raw.start, 1);
        destroyed = soundPool.load(context, R.raw.destroyed, 1);
        win = soundPool.load(context, R.raw.win, 1);
        bump = soundPool.load(context, R.raw.bump, 1);
        boost = soundPool.load(context, R.raw.engine, 1);

    }
    public void stopSound(int streamid){
        this.soundPool.stop(streamid);
    }
    public int playSound(String sound){
        // TODO add some methods for stopping sound?
        switch (sound){

            case "start":
                streamid = this.soundPool.play(start, 1, 1, 1, 0, 1f);
                break;

            case "win":
                streamid = this.soundPool.play(win, 1, 1, 1, 0, 1f);
                break;

            case "bump":
                streamid = this.soundPool.play(bump, 1, 1, 1, 0, 1f);
                break;

            case "crash":
                streamid = this.soundPool.play(destroyed, 1, 1, 1, 0, 1f);
                break;

            case "boost":
                streamid = this.soundPool.play(boost, 1, 1, 1, 0, 1f);
                break;

        }
        return streamid;

    }
}// End SoundManager
