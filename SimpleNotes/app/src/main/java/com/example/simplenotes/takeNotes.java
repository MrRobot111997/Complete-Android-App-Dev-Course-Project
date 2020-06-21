package com.example.simplenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class takeNotes extends AppCompatActivity {

    EditText editTextMultipleLines ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_notes);

        editTextMultipleLines = findViewById(R.id.editTextTextMultiLine) ;
    }

    public void save(View view){

        if( editTextMultipleLines.getText().toString().equals("")){
            Toast.makeText(this , "You Didn't Write any Notes" , Toast.LENGTH_SHORT).show();
            Log.i("Text" , "No Notes" ) ;
        }
        else{
            Log.i("Text" , editTextMultipleLines.getText().toString()) ;
            MainActivity.notesList.add( editTextMultipleLines.getText().toString() ) ;
            MainActivity.arrayAdapter.notifyDataSetChanged();

            try {
                MainActivity.sharedPreferences.edit().putString("notes" , ObjectSerializer.serialize(MainActivity.notesList)).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finish() ;
        }
    }

}