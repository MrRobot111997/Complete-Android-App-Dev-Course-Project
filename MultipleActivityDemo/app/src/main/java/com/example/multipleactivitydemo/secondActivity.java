package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class secondActivity extends AppCompatActivity {

    public void back(View view) {

//        First way
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent) ;


        //Second Way
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        String name = intent.getStringExtra("friend") ;


        Toast.makeText(this , name , Toast.LENGTH_SHORT).show();
    }
}