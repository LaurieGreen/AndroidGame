package com.example.alyn.alphabuild;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laurie on 05-Jul-16.
 */
public class DustEngine {
    public List<SpaceDust> dustList;
    public int getDustListSize()
    {
        return dustList.size();
    }
    int numSpecs = 1000;


    public DustEngine(Context context, int screensizex, int screensizey)
    {
        dustList = new ArrayList<SpaceDust>();
        for (int i = 0; i < numSpecs; i++)
        {
            // Where will the dust spawn?
            SpaceDust spec = new SpaceDust(screensizex, screensizey);
            dustList.add(spec);
        }
    }

    public void update(int playerspeed)
    {
        for (int i = 0; i < dustList.size(); i++)
        {
            dustList.get(i).update(playerspeed);
        }
    }
    public void draw(Canvas canvas, Paint paint)
    {
        for (int i = 0; i < dustList.size(); i++)
        {
            canvas.drawPoint(dustList.get(i).getX(), dustList.get(i).getY(), paint);
        }
    }
}
