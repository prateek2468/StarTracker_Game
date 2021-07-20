package edu.csub.startracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable{
    // 2 background objects
    private final Background background1;
    private final Background background2;
    private boolean isPlaying = true;
    private Thread thread;
    private int touchX , touchY;
    private final Player player;
    private ArrayList<Laser> lasers;
    private ArrayList<GameObject> enemies;
    private GameActivity gameActivity;
    private final float screenWidth , screenHeight;
    private HighScore highScore = HighScore.getInstance();

    EnemySpawner es;
    private Paint textPaint = new Paint();
    private Paint highScorePaint = new Paint();


    public GameView(GameActivity context , int screenX , int screenY) {
        super(context);
        Resources res = getResources();
        screenWidth = res.getDisplayMetrics().widthPixels;
        screenHeight = res.getDisplayMetrics().heightPixels;
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(screenWidth * 0.1f);
        highScorePaint.setColor(Color.WHITE);
        highScorePaint.setTextSize(screenWidth * 0.04f);

        //setting the background
        background1 = new Background(screenX , screenY , res);
        background2 = new Background(screenX , screenY , res);

        //setting the background a lil off the screen
        background2.setY(screenY);

        player = new Player(res);
        es = new EnemySpawner(res );

        lasers = player.getLasers();
        enemies = es.getEnemies();

        gameActivity = context;




    }

    @Override
    public void run() {
        while(isPlaying){
            // update the playing screen and objects
            update();
            draw();
            sleep();
        }

    }
    private void update(){
        background1.update();
        background2.update();
        player.updateTouch(touchX ,touchY);
        player.update();
        es.update();
        checkAllCollisions();
        checkEnemiesOffScreen();
    }

    private void checkEnemiesOffScreen() {

        for(GameObject go : enemies){
            if(go.getY() > screenHeight){
                player.takeDamage(100);
                go.takeDamage(100);
                gameActivity.GameOver();
            }
        }
    }

    private void checkAllCollisions() {
        // checking collision of laser and enemy
        for(Laser laser : lasers){
            for(GameObject ga : enemies){
                if(checkCollision(laser , ga)){
                    laser.takeDamage(100);
                    ga.takeDamage(25);
                    highScore.addScore(25);
                }
            }
        }
        // checking collision of player with enemy
        for(GameObject go : enemies){
             if(checkCollision(player , go)){
                 player.takeDamage(100);
                 go.takeDamage(100);
                 gameActivity.GameOver();
             }
        }


    }

    private boolean checkCollision(GameObject g1  , GameObject g2){
            return g1.getX() < g2.getX() + g2.getWidth() &&
                    g1.getX() + g1.getWidth() > g2.getX() &&
                    g1.getY() < g2.getY() + g2.getHeight() &&
                    g1.getY() + g1.getHeight() > g2.getY();
    }

    private void draw(){
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            background1.draw(canvas);
            background2.draw(canvas);

            if(!player.isAlive()){
                canvas.drawText("GAME OVER" , screenWidth / 4f , screenHeight / 2f , textPaint);
            }
            canvas.drawText(String.format("Score: %s" ,
                     highScore.getCurScore() ) , screenWidth * 0.02f
                            , screenHeight * 0.06f , highScorePaint);
            player.draw(canvas);
            es.draw(canvas);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause(){
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        touchX = (int)event.getX();
        touchY = (int)event.getY();
        return true;
    }
}
