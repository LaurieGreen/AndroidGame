package com.example.alyn.alphabuild;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laurie on 05-Jul-16.
 */
public class EnemyEngine {
    public List<EnemyShip> enemyList;
    public int getEnemyListSize()
    {
        return enemyList.size();
    }


    public EnemyEngine(Context context, int screensizex, int screensizey){
        enemyList = new ArrayList<EnemyShip>();
        enemyList.add(new EnemyShip(context, screensizex, screensizey));
        enemyList.add(new EnemyShip(context, screensizex, screensizey));
        enemyList.add(new EnemyShip(context, screensizex, screensizey));
        if(screensizex > 1000){
            enemyList.add(new EnemyShip(context, screensizex, screensizey));
        }

        if(screensizex > 1200){
            enemyList.add(new EnemyShip(context, screensizex, screensizey));
        }
    }

    public void update(Context context, int screensizex, int screensizey, int playerspeed){
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).update(playerspeed);
            if (!enemyList.get(i).getActive()) {
                enemyList.remove(i);
                enemyList.add(new EnemyShip(context, screensizex, screensizey));
            }
        }
    }
}
