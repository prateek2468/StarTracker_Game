package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Enemy02 implements GameObject{
    private float x , y, ySpeed;
    private float health = 100;
    private final Bitmap enemy;
    private final Bitmap enemy_fast;
    private Bitmap curImage;
    private final int screenWidth , screenHeight , dpi;
    private final int width , height;
    private int frameTick= 0 , launchTick;
    private Paint paint = new Paint();

    public Enemy02(Resources res,float x, float y){
        //setting the values of the width , height of the screen
        dpi = res.getDisplayMetrics().densityDpi;
        screenHeight = res.getDisplayMetrics().heightPixels;
        screenWidth = res.getDisplayMetrics().widthPixels;

        enemy = BitmapFactory.decodeResource(res , R.mipmap.enemy02);
        enemy_fast = BitmapFactory.decodeResource(res , R.mipmap.enemy02_fast);
        curImage = enemy;

        width = curImage.getWidth();
        height = curImage.getHeight();

        this.x = x;
        this.y = y;

        ySpeed = 0.01f * dpi;
        launchTick = new Random().nextInt(120-30)+30;

    }


    @Override
    public void update() {
       // start slow and wait some time
        frameTick++;
        if(frameTick == launchTick) {
            //switch image and go fast
            curImage = enemy_fast;
            ySpeed *= 4f;
        }
        // move on the y
        y += ySpeed;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(curImage , x, y , paint);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean isAlive() {
        return health > 0f;
    }

    @Override
    public float getHealth() {
        return health;
    }

    @Override
    public float takeDamage(float dam) {
        return health -= dam;
    }

    @Override
    public float addHealth(float repairAmount) {
        return health += repairAmount;
    }
}
