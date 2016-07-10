package com.example.alyn.alphabuild;

/**
 * Created by Alyn on 28/06/16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class PlayerShip {
    private Bitmap bitmap;
    private SpriteSheetManager mSpriteSheetManager;
    private int x, y;
    private int speed;

    private int shieldStrength;
    private boolean boosting;

    private final int GRAVITY = -15;

    // Stop ship leaving the screen
    private int maxY;
    private int minY;


    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 100;


    private int frameWidth = 100;
    private int frameHeight = 50;

    // How many frames are there on the sprite sheet?
    private int frameCount = 5;

    // Start at the first frame - where else?
    private int currentFrame = 0;

    // What time was it when we last changed frames
    private long lastFrameChangeTime = 0;

    // How long should each frame last
    private int frameLengthInMilliseconds = 100;

    // A rectangle to define an area of the
    // sprite sheet that represents 1 frame
    private Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);

    // A hit box for collision detection
    private Rect hitBox;

    // Constructor
    public PlayerShip(Context context, int screenX, int screenY)
    {
        boosting = false;
        x = 50;
        y = 50;

        shieldStrength = 10;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship1);

        maxY = screenY - bitmap.getHeight();
        minY = 0;
        mSpriteSheetManager = new SpriteSheetManager(context, x, y, 8, 200, 100);

        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update()
    {
        if (boosting)
        {
            speed += 2;
        }
        else
        {
            speed -= 5;
        }

        if (speed > MAX_SPEED)
        {
            speed = MAX_SPEED;
        }

        if (speed < MIN_SPEED)
        {
            speed = MIN_SPEED;
        }

        // fly up or down
        y -= speed + GRAVITY;

        // Don't let ship stray off screen
        if (y < minY)
        {
            y = minY;
        }

        if (y > maxY)
        {
            y = maxY;
        }

        // Refresh hit box location
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

        //update the animation manager to get updated frames and rect
        //pass in players location
        mSpriteSheetManager.update(x,y);

    }

    public void draw(Canvas canvas, Paint paint)
    {
        // gets the current frame and rect from the spritesheet manager object
        canvas.drawBitmap(
                mSpriteSheetManager.getBitmap(),
                mSpriteSheetManager.getFrameToDraw(),
                mSpriteSheetManager.getWhereToDraw(),
                paint);
    }

    public void setBoosting()
    {
        boosting = true;
    }

    public boolean isBoosting() {
        return boosting;
    }

    public void stopBoosting()
    {
        boosting = false;
    }

    //Getters
    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Rect getHitbox(){
        return hitBox;
    }

    public int getShieldStrength()
    {
        return shieldStrength;
    }

    public void reduceShieldStrength(){
        shieldStrength --;
    }

}