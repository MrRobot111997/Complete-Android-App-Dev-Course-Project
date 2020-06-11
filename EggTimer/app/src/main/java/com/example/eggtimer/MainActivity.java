package com.example.eggtimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    SeekBar timeBar ;
    TextView tv;
    CountDownTimer counter;
    boolean state = false ;

    public void buttonClick(View view) {
        final Button button = (Button) findViewById(R.id.button);

        if (!state) {
            state = true ;
            button.setText("Stop");
            counter = new CountDownTimer(timeBar.getProgress()*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeBar.setProgress(timeBar.getProgress()-1);
                    textUpdate((int)millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    timeBar.setProgress(0);
                    state = false;
                    button.setText("Start");
                }
            }.start();
        }
        else{
            state = false ;
            button.setText("Start");
            timeBar.setProgress(timeBar.getProgress());
            counter.cancel();
        }
    }

    public void textUpdate(int seconds){
        seconds = seconds/1000;
        int mins = seconds/60 ;
        int secs = seconds-(mins*60) ;

        if(secs<10){
            tv.setText(mins+":0"+secs);
        }
        else {
            tv.setText(mins + ":" + secs);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeBar = findViewById(R.id.timeBar) ;
        tv = findViewById(R.id.timerView);
        timeBar.setMin(0); timeBar.setMax(600);

        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textUpdate(progress*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}