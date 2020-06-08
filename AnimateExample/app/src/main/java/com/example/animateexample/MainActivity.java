package com.example.animateexample;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean isBruce = true ;

    public void toBatman(View view){
        ImageView iV = (ImageView) findViewById(R.id.imageView);
        ImageView iV1 = (ImageView) findViewById(R.id.imageView2);

        if(isBruce){
            isBruce =false;
            iV.animate().alpha(0).setDuration(2000);
            iV1.animate().alpha(1).setDuration(2000);
        }
        else{
            isBruce = true;
            iV.animate().alpha(1).setDuration(2000);
            iV1.animate().alpha(0).setDuration(2000);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iV = (ImageView) findViewById(R.id.imageView);
        iV.setX(-1000);
        iV.animate().translationXBy(1000).rotation(1800).setDuration(1000);
    }
}