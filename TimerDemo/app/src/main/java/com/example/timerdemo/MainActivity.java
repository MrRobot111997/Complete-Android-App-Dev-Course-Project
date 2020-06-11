package com.example.timerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int t = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView tv = (TextView) findViewById(R.id.timerView) ;

/*      FIRST WAY USING HANDLER AND RUN :
        final Handler handle = new Handler() ;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                tv.setText(Integer.toString(t));
                t++;
                handle.postDelayed(this, 1000) ;
            }
        };
        handle.post(run);
*/
    //Using Count Down Timer

        new CountDownTimer(10000 , 1000){
            public void onTick(long currentSecond){
                tv.setText("Second = "+String.valueOf(currentSecond/1000));
            }

            public void onFinish(){
                tv.setText("Times UP !!");
            }
        }.start();

    }
}