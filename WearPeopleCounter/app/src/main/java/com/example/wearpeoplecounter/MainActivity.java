package com.example.wearpeoplecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
//    Button increment = findViewById(R.id.increment) ;
//    Button decrement = findViewById(R.id.decrement) ;
    int  count = 0 ;

    public void incrementer(View view){
        count++ ;
        mTextView.setText(Integer.toString(count));
    }
    public void decrement(View view){
        if( count != 0 ) count-- ;
        mTextView.setText(Integer.toString(count));
    }

    public void getHint(View view){
        Toast.makeText(this, "Long Press the Count to Reset it" , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTextView.setText("0");
                count = 0 ;
                return false;
            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }
}
