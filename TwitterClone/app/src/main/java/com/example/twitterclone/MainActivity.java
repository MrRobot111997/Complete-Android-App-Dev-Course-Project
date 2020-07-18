package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText username , password ;
    ParseUser user ;
    String userName , passWord ;
    Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Login / SignUp");

        username = findViewById(R.id.editTextUsername) ;
        password = findViewById(R.id.editTextPassword) ;


        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                                .applicationId("myappID")
                                .clientKey("3dzNsO0Qif0e")
                                .server("http://18.220.7.107/parse")
                                .build());

        if(ParseUser.getCurrentUser()!=null){
            redirectUser();
        }

    }

    public void loginSignUp(View view){

        userName = username.getText().toString() ;
        passWord = password.getText().toString() ;

        user = new ParseUser() ;

        if( userName.equals("") || passWord.equals("")){
            Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }
        else{
            ParseUser.logInInBackground(userName, passWord, new LogInCallback() {
                @Override
                public void done(ParseUser users , ParseException e) {
                    if( users != null && e == null ){
                        Log.i("what happened" , "Login") ;
                        Toast.makeText(MainActivity.this, "Welcome Back! "+userName , Toast.LENGTH_SHORT).show();
                        redirectUser();
                    }
                    else{
                        user.setUsername(userName);
                        user.setPassword(passWord);

                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if( e==null ){
                                    Toast.makeText(MainActivity.this, "Welcome "+userName , Toast.LENGTH_SHORT).show();
                                    Log.i("what Happedned" , "Signup") ;
                                    redirectUser();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Username already Exists", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                    Log.i("what Happedned" , "Signup Failed") ;
                                }
                            }
                        });
                    }
                }
            });

        }
    }

    public void redirectUser(){
        intent = new Intent(this , usersList.class) ;
        startActivity(intent);
    }

}