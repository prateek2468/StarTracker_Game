package edu.csub.startracker;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private HighScore highScore = HighScore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // making the screen full and removing the top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // high Score display

    @Override
    protected  void onResume(){
        super.onResume();
        getTopScores(10);

        TextView tvHighScore = findViewById(R.id.tvHighScore);
        EditText etPlayerName = findViewById(R.id.etPlayerName);
        etPlayerName.setText(highScore.getPlayerName());
        tvHighScore.setText(String.format("High Score : %s" , highScore.gethighScore()));

        if(highScore.gethighScore() != 0 && highScore.gethighScore() == highScore.getCurScore()){
            highScore.postHighScore();
        }
    }

    private void getTopScores(int how) {
        ListView highScores = findViewById(R.id.lvTopScores);
        highScore.getHighScore(how , highScores , this);
    }


    public void onPlayButtonClicked(View view) {
        highScore.resetCurScore();
        EditText etPlayerName = findViewById(R.id.etPlayerName);
        highScore.setPlayerName(etPlayerName.getText().toString());
        startActivity(new Intent(MainActivity.this , GameActivity.class));


    }
}