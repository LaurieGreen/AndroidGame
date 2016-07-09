package com.example.alyn.alphabuild;

/**
 * Created by Alyn on 28/06/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable {

    private boolean gameEnded;
    private boolean boostisplaying = false;

    private Context context;

    private int screenX;
    private int screenY;

    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    volatile boolean playing;
    Thread gameThread = null;

    // Game objects
    private PlayerShip mPlayer;
    public EnemyEngine mEnemyEngine;
    public DustEngine mDustEngine;

    // Make some random space dust
    ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;


    // This variable tracks the game frame rate
    long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;


    // For saving and loading the high score
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    //SoundEngine
    SoundManager sm;

    TDView(Context context, int x, int y)
    {
        super(context);
        this.context  = context;

        screenX = x;
        screenY = y;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        //Initialise sm
        sm = new SoundManager();
        sm.loadSound(context);


        // Initialise our mPlayer ship
        //mPlayer = new PlayerShip(context, x, y);
        //enemy1 = new EnemyShip(context, x, y);
        //enemy2 = new EnemyShip(context, x, y);
        //enemy3 = new EnemyShip(context, x, y);

        //int numSpecs = 40;

        //for (int i = 0; i < numSpecs; i++) {
        // Where will the dust spawn?
        //SpaceDust spec = new SpaceDust(x, y);
        //dustList.add(spec);
        //}

        // Load fastest time
        prefs = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);
        // Initialize the editor ready
        editor = prefs.edit();
        // Load fastest time
        // if not available our highscore = 1000000
        fastestTime = prefs.getLong("fastestTime", 1000000);

        startGame();
    }

    private void startGame(){

        //Initialise game objects
        mPlayer = new PlayerShip(context, screenX, screenY);
        mEnemyEngine = new EnemyEngine(context, screenX, screenY);
        mDustEngine = new DustEngine(context, screenX, screenY);

        // Reset time and distance
        distanceRemaining = 30000;// 30 km
        timeTaken = 0;
        sm.playSound("start");

        // Get start time
        timeStarted = System.currentTimeMillis();

        gameEnded = false;

    }

    @Override
    public void run()
    {
        while (playing)
        {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            update();

            // Draw the frame
            draw();
            control();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;

            if (timeThisFrame >= 1)
            {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    // Everything that needs to be updated goes in here
    private void update()
    {
        if(!gameEnded)
        {
            // Collision detection on new positions
            // Before move because we are testing last frames
            // position which has just been drawn
            boolean hitDetected = false;

            for (int i = 0; i < mEnemyEngine.enemyList.size(); i++)
            {
                if (Rect.intersects(mPlayer.getHitbox(), mEnemyEngine.enemyList.get(i).getHitbox()))
                {
                    hitDetected = true;
                    mEnemyEngine.enemyList.get(i).setActive(false);
                }
            }
            if (hitDetected)
            {
                //int streamId = this.soundPool.play(this.bump, 1, 1, 1, 0, 1f);
                mPlayer.reduceShieldStrength();
                sm.playSound("bump");
                if (mPlayer.getShieldStrength() < 0)
                {
                    //game over so do something
                    //streamId = this.soundPool.play(this.destroy, 1, 1, 1, 0, 1f);
                    gameEnded = true;
                }
            }
            //update game objects
            mPlayer.update();
            mEnemyEngine.update(context, screenX, screenY, mPlayer.getSpeed());


            //subtract distance to home planet based on current speed
            distanceRemaining -= mPlayer.getSpeed();

            //How long has the mPlayer been flying
            timeTaken = System.currentTimeMillis() - timeStarted;

            if (mPlayer.isBoosting() && !boostisplaying)
            {
                sm.playSound("boost");
                boostisplaying = true;
            }
            else
            {
                boostisplaying = false;
            }

            //Completed the game!
            if(distanceRemaining < 0)
            {
                sm.playSound("win");
                //int streamId = this.soundPool.play(this.win, 1, 1, 1, 0, 1f);
                //check for new fastest time
                if(timeTaken < fastestTime)
                {
                    // Save high score
                    editor.putLong("fastestTime", timeTaken);
                    editor.commit();
                    fastestTime = timeTaken;
                }

                // avoid ugly negative numbers
                // in the HUD
                distanceRemaining = 0;

                // Now end the game
                gameEnded = true;
            }
        }
        //update whether game over or not
        mDustEngine.update(mPlayer.getSpeed());
    }

    private void draw()
    {
        if (ourHolder.getSurface().isValid())
        {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            // Rub out the last frame
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            // Draw Hit boxes
            //canvas.drawRect(mPlayer.getHitbox().left, mPlayer.getHitbox().top, mPlayer.getHitbox().right, mPlayer.getHitbox().bottom, paint);
            //canvas.drawRect(enemy1.getHitbox().left, enemy1.getHitbox().top, enemy1.getHitbox().right, enemy1.getHitbox().bottom, paint);
            //canvas.drawRect(enemy2.getHitbox().left, enemy2.getHitbox().top, enemy2.getHitbox().right, enemy2.getHitbox().bottom, paint);
            //canvas.drawRect(enemy3.getHitbox().left, enemy3.getHitbox().top, enemy3.getHitbox().right, enemy3.getHitbox().bottom, paint);


            // White specs of dust
            paint.setColor(Color.argb(255, 255, 255, 255));

            //Draw the dust from our arrayList
            mDustEngine.draw(canvas, paint);

            if(!gameEnded)
            {
                // Draw the mPlayer and enemies
                mEnemyEngine.draw(canvas, paint);
                mPlayer.draw(canvas, paint);

                // Draw the hud
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                //canvas.drawText("Fastest:" + fastestTime + "s", 10, 20, paint);
                canvas.drawText("Fastest:" + formatTime(fastestTime) + "s", 10, 20, paint);
                //canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20, paint);
                canvas.drawText("Time:" + formatTime(timeTaken) + "s", screenX / 2, 20, paint);
                canvas.drawText("Distance:" + distanceRemaining / 1000 + " KM", screenX / 3, screenY - 20, paint);
                canvas.drawText("Shield:" + mPlayer.getShieldStrength(), 10, screenY - 20, paint);
                canvas.drawText("Speed:" + mPlayer.getSpeed() * 60 + " MPS", (screenX / 3) * 2, screenY - 20, paint);
            }
            else
            {
                // Show pause screen
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX/2, 100, paint);
                paint.setTextSize(25);
                //canvas.drawText("Fastest:"+ fastestTime + "s", screenX/2, 160, paint);
                canvas.drawText("Fastest:"+ formatTime(fastestTime) + "s", screenX/2, 160, paint);
                //canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 200, paint);
                canvas.drawText("Time:" + formatTime(timeTaken) + "s", screenX / 2, 200, paint);
                canvas.drawText("Distance remaining:" + distanceRemaining/1000 + " KM",screenX/2, 240, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenX/2, 350, paint);
            }
            // Display the current fps on the screen
            canvas.drawText("FPS:" + fps, 20, 40, paint);
            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control()
    {
        try
        {
            gameThread.sleep(17);
        }
        catch (InterruptedException e)
        {

        }
    }

    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        // There are many different events in MotionEvent
        // We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Has the mPlayer lifted there finger up?
            case MotionEvent.ACTION_UP:
                mPlayer.stopBoosting();
                //sm.stopSound(sm.playSound("boost"));
                break;

            // Has the mPlayer touched the screen?
            case MotionEvent.ACTION_DOWN:
                mPlayer.setBoosting();
                // If we are currently on the pause screen, start a new game
                if(gameEnded){
                    startGame();
                }
                break;
        }
        return true;
    }

    // Clean up our thread if the game is interrupted or the mPlayer quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    private String formatTime(long time){
        long seconds = (time) / 1000;
        long thousandths = (time) - (seconds * 1000);
        String strThousandths = "" + thousandths;
        if (thousandths < 100){strThousandths = "0" + thousandths;}
        if (thousandths < 10){strThousandths = "0" + strThousandths;}
        String stringTime = "" + seconds + "." + strThousandths;
        return stringTime;
    }

}
