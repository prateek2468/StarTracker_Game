package edu.csub.startracker;

import android.content.Context;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//singleton class
public  final class HighScore {

    private static final HighScore INSTANCE = new HighScore();
    private int curScore =0 ;
    private int HighScore =0 ;
    private String name = "Player 1";
    private FirebaseFirestore db;

    private HighScore(){
       db = FirebaseFirestore.getInstance();
    }
    public static HighScore getInstance(){
        return INSTANCE;
    }

    public void addScore(int score){
        curScore += score;
        if(curScore > HighScore){
            HighScore = curScore;
        }
    }

    public int getCurScore() {
        return curScore;
    }

    public int gethighScore() {
        return HighScore;
    }

    public void resetCurScore() {
        curScore = 0;
    }


    public void setPlayerName(String playerName) {
        name = playerName;
    }

    public String  getPlayerName() {
        return name;
    }

    public void getHighScore(int how , final ListView highScores , final Context context){
        final ArrayList<String> topScores = new ArrayList<>();

        db.collection("HighScore")
                .orderBy("score" , Query.Direction.DESCENDING)
                .limit(how)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot doc : task.getResult()){
                                String tmpString = String.format("%s: %s" , doc.getId() ,
                                        doc.get("score"));
                                topScores.add(tmpString);

                            }
                            ArrayAdapter<String> itemsAdaptor = new ArrayAdapter<String >(context ,
                                    android.R.layout.simple_list_item_1 , topScores);
                            highScores.setAdapter(itemsAdaptor);
                        }
                    }
                });
    }


    public void postHighScore(){
        Map<String , Integer> hs = new HashMap<>();
        hs.put("score" , HighScore );

        // highscore with a particular user name
        db.collection("HighScore").document(name)
                .set(hs)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }
}
