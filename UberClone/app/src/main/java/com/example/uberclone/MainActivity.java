package com.example.uberclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    Switch aSwitch ;
    Button button ;
    String riderState = "Customer" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        button = findViewById(R.id.button) ;
        aSwitch = findViewById(R.id.switch1) ;
        Log.i("Switch Value" , String.valueOf(aSwitch.isChecked())) ;

        if(ParseUser.getCurrentUser() == null){
            login() ;
        }
        else if( ParseUser.getCurrentUser().get("riderOrdriver") != null ){
            Log.i("Info" , "Redirecting as "+ParseUser.getCurrentUser().get("riderOrdriver")) ;
        }

    }

    public void login(){
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if( user != null && e == null ){
                    Log.i( "Anonymous Login" , "Successful" ) ;
                }
                else{
                    e.printStackTrace();
                    Log.i("Anonymous Login:" , "Failed") ;
                }
            }
        });
    }

    public void getStarted( View view ){

        if( ParseUser.getCurrentUser() == null ){
            login();
        }


        if( aSwitch.isChecked() ){
            riderState = "Driver" ;
            ParseUser.getCurrentUser().put("riderOrdriver" , riderState);
            Intent i = new Intent(this , requestsView.class) ;
            startActivity(i);
        }
        else {
            Log.i("Info", "Redirecting as " + riderState);
            ParseUser.getCurrentUser().put("riderOrdriver" , riderState);
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }
    }
}