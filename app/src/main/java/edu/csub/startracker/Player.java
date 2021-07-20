package edu.csub.startracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Iterator;

public class Player implements GameObject{

    private float x , y , prevX , prevY;
    private final Bitmap PlayerImg;
    private  Paint paint = new Paint();
    private final Bitmap playerLeft;
    private final Bitmap playerRight;
    private Bitmap curImage;
    private final float dpi;
    private int frameTicks = 0 , shotTicks = 0;
    private Resources res;
    private final int  width , height;

    ArrayList<Laser> lasers = new ArrayList<>();
    private float health = 100f;


    public Player(Resources res ) {

        PlayerImg = BitmapFactory.decodeResource(res , R.mipmap.player);
        playerLeft = BitmapFactory.decodeResource(res , R.mipmap.player_left);
        playerRight = BitmapFactory.decodeResource(res , R.mipmap.player_right);
        this.res = res;
        curImage = PlayerImg;
        width = curImage.getWidth();
        height = curImage.getHeight();
        // initially putting the ship at the center bottom
        DisplayMetrics dm = res.getDisplayMetrics();
        dpi = dm.densityDpi;
        x = (dm.widthPixels / 2f) - (PlayerImg.getWidth() / 2f);
        y = (dm.heightPixels *0.75f);

    }

    public void updateTouch(int touchX , int  touchY){
        if(touchX > 0 && touchY>0 ){
            // centering the ship to the touch and above it
            this.x = touchX - (PlayerImg.getWidth() / 2f);
            this.y = touchY - (PlayerImg.getHeight() *2f);
        }
    }
    @Override
    public void update(){
        if(health <= 0)return;

        // tilting of ship in the desired direction

        if(Math.abs(x - prevX)< 0.04*dpi){
            frameTicks++;
        }else{
            frameTicks = 0;
        }

        if(this.x < prevX - 0.04 * dpi){
            curImage = playerLeft;
        }else if(this.x > prevX + 0.04 *dpi){
            curImage = playerRight;
        }else if(frameTicks >5){
            curImage = PlayerImg;
        }


        prevX = x;
        prevY = y;

        //increse shotTicks
        shotTicks++;

        // see if we need  to shoot
            if(shotTicks >= 20){
                //shoot shotticks
                    Laser tmp = new Laser(this.res);
                    tmp.setX(x + (PlayerImg.getWidth() / 2f) - tmp.getMidX() );
                    tmp.setY(y - tmp.getHeight() / 2f);

                    lasers.add(tmp);
                // reset the shotticks
                shotTicks = 0;
            }

        // remove lasers that were off screen
        for(Iterator<Laser> iterator = lasers.iterator() ; iterator.hasNext() ; ){
            Laser laser = iterator.next();
            if(!laser.isOnScreen() || !laser.isAlive()){
                iterator.remove();
            }
        }


        //update all lasers
        for(Laser laser: lasers){
            laser.update();
        }

    }



    public void draw(Canvas canvas){
        if(health<= 0 )return;
        canvas.drawBitmap(curImage , this.x , this.y , paint);

        //draw all lasers
        for(Laser laser: lasers){
            laser.draw(canvas);

        }
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

    public ArrayList<Laser> getLasers(){
        return lasers;
    }
}
