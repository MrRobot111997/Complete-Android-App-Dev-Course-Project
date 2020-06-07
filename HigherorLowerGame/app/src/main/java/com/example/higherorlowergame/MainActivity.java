package com.example.higherorlowergame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean firstTry = false ;
    int holdRandom ;
    public void resetGame(View view){
        firstTry = false ;
    }

    public void playGame( View view ){

        if(firstTry==false){
            firstTry = true ;
            holdRandom = (int) (Math.random() * (21)+1) ;
        }

        EditText eT = (EditText) findViewById(R.id.editTextTextPersonName);

       if(eT.getText().toString().equals("")){
            Toast.makeText(this, "Oops!!\nYou Didn't Make A Guess..." , Toast.LENGTH_LONG).show();
        }
        else{
            try{
                int holdGuess = Integer.parseInt(eT.getText().toString()) ;
                Log.i("Guess ", holdRandom+" "+holdGuess) ;
                if( holdGuess == holdRandom ){
                    Toast.makeText(this, "This is the Correct Guess" , Toast.LENGTH_LONG).show();
                }
                else if(holdGuess < holdRandom){
                    Toast.makeText(this , "Nice Guess!\nBut Go Higher" , Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this , "Hice Guess!!\nBut Go Lower" , Toast.LENGTH_LONG ).show();
                }
            }
            catch(NumberFormatException e) {
                Toast.makeText(this, "Your Guess Should Be a No.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}