package com.example.videoplaybackdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView VV = (VideoView) findViewById(R.id.videoView);

        VV.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.video);

        Toast.makeText(this, "android.resource://" + getPackageName() + "/" + R.raw.video , Toast.LENGTH_LONG).show();
        Log.i("Address" , "android.resource://" + getPackageName() + "/" + R.raw.video  );

        MediaController mediaControl = new MediaController(this);

        mediaControl.setAnchorView(VV);

        VV.setMediaController(mediaControl);

        VV.start();

    }
}