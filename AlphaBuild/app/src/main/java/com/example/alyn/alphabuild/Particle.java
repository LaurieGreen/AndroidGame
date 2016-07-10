package com.example.alyn.alphabuild;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Laurie on 10-Jul-16.
 */
public class Particle {
    private int x, y;
    private int speedx, speedy;
    int timeToLive;

    // Detect dust leaving the screen
    private int maxX;
    private int maxY;

    public Particle(int screenX, int screenY, int positionx, int positiony, int timeToLive)
    {
        x = positionx;
        y = positiony;
        this.timeToLive = timeToLive;
        maxX = screenX;
        maxY = screenY;
    }

    public void update(int playerSpeed){
        timeToLive--;
        x -= playerSpeed;
        x -= speedx;
        y -= speedy;
    }

    //Getters and Setters
    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }
}
