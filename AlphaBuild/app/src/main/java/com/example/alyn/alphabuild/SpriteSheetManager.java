package com.example.alyn.alphabuild;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Laurie on 09-Jul-16.
 */
public class SpriteSheetManager {

    // How many frames are there on the sprite sheet?
    private int frameCount;

    private int frameWidth;
    private int frameHeight;

    Bitmap spriteSheet;
    Bitmap bitmap;

    RectF whereToDraw;

    // Start at the first frame - where else?
    private int currentFrame = 0;

    // What time was it when we last changed frames
    private long lastFrameChangeTime = 0;

    // How long should each frame last
    private int frameLengthInMilliseconds = 100;

    // A rectangle to define an area of the
    // sprite sheet that represents 1 frame
    private Rect frameToDraw;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public RectF getWhereToDraw() {
        return whereToDraw;
    }

    public Rect getFrameToDraw() {
        return frameToDraw;
    }

    public SpriteSheetManager(Context context, int positionx, int positiony, int frameCount, int frameWidth, int frameHeight, Bitmap spriteSheet) {
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        frameToDraw = new Rect(
                0,
                0,
                frameWidth,
                frameHeight);

        // A rect that defines an area of the screen
        // on which to draw
        whereToDraw = new RectF(
                positionx,
                positiony,
                positionx + frameWidth,
                frameHeight + positiony);

        bitmap = Bitmap.createScaledBitmap(spriteSheet,
                frameWidth * frameCount,
                frameHeight,
                false);

    }

    public void getCurrentFrame() {

        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame++;
            if (currentFrame >= frameCount) {

                currentFrame = 0;
            }
        }
        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    //must be updated to get current frames and current rect
    public void update(int positionx, int positiony) {
        whereToDraw.set(
                positionx,
                positiony,
                positionx + frameWidth,
                frameHeight + positiony);

        getCurrentFrame();

    }
}
