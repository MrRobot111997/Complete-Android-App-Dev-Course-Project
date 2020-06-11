package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView time ,que ,qCount ,tl ,q, s , out ;
    GridLayout answerGrid ;
    Button start ;
    int answer , correct = 0 , total ;
    int[] arr = {R.id.button1 , R.id.button2 , R.id.button3 , R.id.button4} ;

    public void start(View view){
        answerGrid = (GridLayout) findViewById(R.id.answerGrid);
        time = (TextView) findViewById(R.id.timeView);
        que = (TextView) findViewById(R.id.queView);
        qCount = (TextView) findViewById(R.id.queCount);
        start = (Button) findViewById(R.id.startButton);
        tl = (TextView) findViewById(R.id.textView4);
        q = (TextView) findViewById(R.id.textView5) ;
        s = (TextView) findViewById(R.id.textView6) ;
        out = (TextView) findViewById(R.id.out);

        answerGrid.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        que.setVisibility(View.VISIBLE);
        qCount.setVisibility(View.VISIBLE);
        start.setVisibility(View.INVISIBLE);
        tl.setVisibility(View.VISIBLE);
        q.setVisibility(View.VISIBLE);
        s.setVisibility(View.VISIBLE);
        out.setText("");

        startTimer();
        quiz();

    }

    public void startTimer(){
        CountDownTimer counter = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int min = (int) millisUntilFinished/1000/60;
                int sec = (int) (millisUntilFinished/1000) - (min * 60);
                if(sec<=9)
                    time.setText(min+":0"+sec);
                else
                    time.setText(min+":"+sec);
            }

            @Override
            public void onFinish() {
                answerGrid.setVisibility(View.INVISIBLE);
                time.setVisibility(View.INVISIBLE);
                que.setVisibility(View.INVISIBLE);
                qCount.setVisibility(View.INVISIBLE);
                tl.setVisibility(View.INVISIBLE);
                q.setVisibility(View.INVISIBLE);
                s.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
                out.setVisibility(View.VISIBLE);
                start.setText("Play\nAgain");
                out.setText(correct+"/"+total);
                correct=total=0;
            }
        }.start();
    }

    public void quiz(){
        Random rand = new Random();
        int a = rand.nextInt(100);
        int b = rand.nextInt(100);
        answer = a+b ;
        que.setText(a+"+"+b);

        Button but ;
        int choice = rand.nextInt(4);
        for(int i=0;i<4;i++){
            but = (Button) findViewById(arr[i]);
            but.setBackgroundResource(R.color.choice);
            if(i==choice){
                but.setText(Integer.toString(answer));
            }
            else{
                int c1 = rand.nextInt(a)+b;
                int c2 = rand.nextInt(b)+a;
                but.setText(Integer.toString(c1+c2));
            }
        }
    }

    public void answer(View view){
        Button select = (Button) view ;

        int ans = Integer.parseInt(select.getText().toString());
        if(ans == answer){
            out.setText("Correct");
            out.setBackgroundResource(R.color.right);
            correct++ ; total++;
        }
        else{
            out.setText("Wrong :(");
            out.setBackgroundResource(R.color.wrong);
            total++;
        }

        qCount.setText(correct+"/"+total);
        quiz();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}