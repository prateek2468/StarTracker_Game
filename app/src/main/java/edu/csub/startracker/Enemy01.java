package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy01 implements GameObject{
    private final float dpi;
    private float x , y , ySpeed;
    private final float width , height;
    private float health = 100f;
    private final Bitmap enemy , enemy_left  , enemy_right ;
    private Bitmap curImage;
    private int screenWidth , screenHeight;
    private Paint paint = new Paint();

    public Enemy01(Resources res , float x, float y){
        this.x = x;
        this.y =y;
        enemy = BitmapFactory.decodeResource(res , R.mipmap.enemy01);
        enemy_left = BitmapFactory.decodeResource(res , R.mipmap.enemy01_left);
        enemy_right = BitmapFactory.decodeResource(res , R.mipmap.enemy01_right);
        curImage = enemy;
        width = curImage.getWidth();
        height = curImage.getHeight();

        dpi = res.getDisplayMetrics().densityDpi;
        screenHeight = res.getDisplayMetrics().heightPixels;
        screenWidth = res.getDisplayMetrics().widthPixels;

        ySpeed = 0.02f * dpi;
    }

    @Override
    public void update() {
        // spiral movement
        float xOff  = (float)  (0.01f * screenHeight * Math.sin(y / (0.04f * screenHeight)));
        x += xOff;
        curImage = xOff >0 ? enemy_left:enemy_right;
        if(Math.abs(xOff) < 2)curImage = enemy;

        y += ySpeed;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(curImage , x , y , paint);
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
