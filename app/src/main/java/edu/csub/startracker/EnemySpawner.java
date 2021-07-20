package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class EnemySpawner {
    // for waves and order of enemies
    private ArrayList<GameObject> enemies;
    float x , y;
    int screenWidth;
    int wave = 1 , enemy01Spawned  =0 , enemy02Spawned = 0;
    Resources res ;
    int frameTick =0 , spawnTick , waveTick = 0 ;
    private Paint paint = new Paint();

    public EnemySpawner(Resources res  ){
        enemies = new ArrayList<>();
        screenWidth = res.getDisplayMetrics().widthPixels;
        this.res = res;
        x = new Random().nextInt((int) (screenWidth * 0.9f - screenWidth *0.1f)) + screenWidth *0.1f ;
        spawnTick = new Random().nextInt(120 - 60)+60;
        paint.setColor(Color.WHITE);
        paint.setTextSize(screenWidth * 0.05f);
    }

    public  void update(){
        frameTick++;


        //spawning logic
        //wait a given amount of time
        if(frameTick >= spawnTick){
            // reset frametick
            frameTick = 0;
            spawnTick = new Random().nextInt(120 - 60)+60;
            //move x to new position
            x = new Random().nextInt((int) (screenWidth * 0.75f - screenWidth *0.05f)) + screenWidth *0.05f ;

            //choose enemy to spawn
            int tmp = (int)Math.round(Math.random());
            if(tmp == 0 && enemy01Spawned < wave){
                //spawn enemy 01
                enemies.add(new Enemy01(res , x, y));
                enemy01Spawned++;
            }else {
                // spawn enemy 2
                tmp =1;
            }

            if(tmp == 1  && enemy02Spawned<wave){
                enemies.add(new Enemy02(res , x, y));
                enemy02Spawned++;
            }




        }

        if(enemy02Spawned >= wave && enemy01Spawned >= wave){
            waveTick++;
        }
        if(waveTick >= 240){
            wave++;
            waveTick =0 ;
            enemy01Spawned =0 ;
            enemy02Spawned = 0;
        }

        // update all enemies
        for(Iterator<GameObject> iterator = enemies.iterator() ; iterator.hasNext() ;){
            GameObject go = iterator.next();
            go.update();
            if(!go.isAlive()){
                iterator.remove();
            }
        }
    }

    public void draw(Canvas canvas){
        // continuous enemy wave
        // displat of number of wave
        canvas.drawText("Wave "+wave , screenWidth* 0.05f , screenWidth * 0.05f , paint);

        for(GameObject go : enemies){
            go.draw(canvas);
        }
    }

    public ArrayList<GameObject> getEnemies(){
        return enemies;
    }
}
