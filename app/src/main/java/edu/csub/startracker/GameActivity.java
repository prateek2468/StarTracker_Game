package edu.csub.startracker;

import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        
        gameView = new GameView(this , point.x , point.y);
        setContentView(gameView);

    }

    public void GameOver(){
        // delay and then end of game
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        } , 6000);
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();
    }
}