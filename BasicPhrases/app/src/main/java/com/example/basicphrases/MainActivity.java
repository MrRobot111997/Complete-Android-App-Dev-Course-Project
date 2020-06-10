package com.example.basicphrases;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer ;
    int[] audio = {R.raw.doyouspeakenglish , R.raw.goodevening , R.raw.hello, R.raw.howareyou ,R.raw.ilivein ,R.raw.mynameis ,
                    R.raw.please , R.raw.welcome} ;

    public void play(View view){

        Button button = (Button) view;
        int tag = Integer.parseInt(button.getTag().toString());

/*
        The Statement Used By Master .
        mediaPlayer = MediaPlayer.create(this,getResources().getIdentifier(nameOfFile , "raw" , getPackageName() )) ;
*/

        for(int i=0;i<8;i++){
            if(tag==i){
                mediaPlayer = MediaPlayer.create(this, audio[i]) ;
                mediaPlayer.start();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}