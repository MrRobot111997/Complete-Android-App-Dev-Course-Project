package com.example.pictureinpicturedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {


    public void goPip(View view){
        enterPictureInPictureMode() ;
        getSupportActionBar().hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        Button onPip = findViewById(R.id.button);
        TextView text = findViewById(R.id.textView) ;

        if ( isInPictureInPictureMode ){
                onPip.setVisibility(View.INVISIBLE);
                text.setText("in PiP View");

            Log.i("Shifted to" , "Pip mode") ;
        }
        else{
            getSupportActionBar().show();
            text.setText("Hello World");
            onPip.setVisibility(View.VISIBLE);
            Log.i("Shifted to" , "Pip mode") ;
        }
    }
}