package com.example.connect3dots;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int currPlayer = 1 , flag = 0;
    int[] gameState = {2 ,2 ,2, 2, 2, 2, 2, 2, 2} ;
    int chanceCounter = 0 ;

    int[][] winStates = {{0,1,2}, {3,4,5} , {6,7,8}, {0,3,6} , {1,4,7}, {2,5,8} , {0,4,8} , {2,4,6} };

    public void dropIn(View view){

        ImageView iv = (ImageView) view ;
        ImageView resultImg = (ImageView) findViewById(R.id.imageView3);
        TextView resultText = (TextView) findViewById(R.id.textView);
        ImageView iV = (ImageView) findViewById(R.id.imageView5);

        if(chanceCounter<=8 && gameState[Integer.parseInt(iv.getTag().toString())]==2 ){

            iv.setTranslationY(-1500);

            gameState[Integer.parseInt(iv.getTag().toString())] = currPlayer ;

            if(currPlayer == 1){
                currPlayer = 0;
                iv.setImageResource(R.drawable.red);
            }
            else{
                currPlayer = 1;
                iv.setImageResource(R.drawable.yellow);
            }

            iv.animate().translationYBy(1500).rotation(1800).setDuration(500);

            if(chanceCounter>=4){
                for ( int[] winState : winStates){
                    if(gameState[winState[0]]==gameState[winState[1]] && gameState[winState[1]]==gameState[winState[2]] && gameState[winState[0]]!=2){

                        if(gameState[winState[0]]==1){
                            resultImg.setImageResource(R.drawable.red);
                            resultText.setText("Red WON!!");
                        }
                        else{
                            resultImg.setImageResource(R.drawable.yellow);
                            resultText.setText("Yellow WON!!");
                        }
                        chanceCounter=9; flag = 1 ;

                        iV.setVisibility(View.VISIBLE);
                    }
                }
            }
            chanceCounter++;
        }
        else if(chanceCounter>8){
            Toast.makeText(this,"Please Reset The Game to Play Further", Toast.LENGTH_SHORT).show();
        }

        if(chanceCounter==8 && flag==0){
            resultText.setText("It's a DRAW");
            resultImg.setImageResource(R.color.red);
            iV.setVisibility(View.VISIBLE);
        }

    }

    public void reset(View view){
        currPlayer = 1 ;
        chanceCounter=0;
        flag=0;

        ImageView resultImg = (ImageView) findViewById(R.id.imageView3);
        resultImg.setImageDrawable(null);
        TextView resultText = (TextView) findViewById(R.id.textView);
        resultText.setText("");
        ImageView iV = (ImageView) findViewById(R.id.imageView5);
        iV.setVisibility(View.INVISIBLE);

        for (int i = 0 ; i < 9 ; i++){
            gameState[i] = 2 ;
        }

        GridLayout row = (GridLayout) findViewById(R.id.gridLayout);

        for(int i=0 ; i < row.getChildCount() ; i++){
            ImageView count = (ImageView) row.getChildAt(i);
            count.setImageDrawable(null);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}