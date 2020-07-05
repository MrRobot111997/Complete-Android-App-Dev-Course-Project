package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener  {

    TextView sign ;
    boolean signStatus = true ;
    Button button ;
    EditText user , pass ;
    ParseUser parseUser ;
    ConstraintLayout background ;
    ImageView image ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Log In");

        sign = findViewById(R.id.signUp) ;
        button = findViewById(R.id.login) ;
        user = findViewById(R.id.username) ;
        pass = findViewById(R.id.password) ;
        pass.setOnKeyListener(this);
        background = findViewById(R.id.background) ;
        image = findViewById(R.id.imageView) ;

  //      background.setOnClickListener(this) ;
    //    image.setOnClickListener(this);


        Parse.initialize(new Parse.Configuration.Builder( this)
                .applicationId("myappID")
                .clientKey("")              //Client Key Removed to For Safe Keeping Just in case .
                .server("http://3.20.204.119/parse")
                .build()
        );
        parseUser = new ParseUser() ;
    }

    public void signup(View view){

        if( view.getId() == R.id.signUp) {
            if (signStatus) {
                sign.setText("or , Log In!");
                button.setText("Sign Up");
                setTitle("Sign Up");
                signStatus = false;
            } else {
                sign.setText("or, Sign Up!");
                button.setText("Log In");
                signStatus = true;
                setTitle("Log In");
            }
        }
        else if( view.getId() == R.id.background || view.getId() == R.id.imageView ){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE) ;
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0) ;
        }
    }

    public void logUp(View view){
        String username = user.getText().toString() ;
        String password = pass.getText().toString() ;

        if(username.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext() , "Please Enter Username/Password" , Toast.LENGTH_SHORT).show();
        }
        else {
            if (signStatus) {
                logIn(username, password);
            } else {
                signUp(username, password);
            }
        }
    }

    public void signUp(String... details){

        parseUser.setUsername(details[0]);
        parseUser.setPassword(details[1]);
        Toast.makeText(getApplicationContext() , details[0]+details[1] , Toast.LENGTH_SHORT).show();
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText( getApplicationContext() , "Signed Up Successfully" , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext() , "Signup Failed ,U" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void logIn( String... details){
        Toast.makeText(getApplicationContext() , details[0]+details[1] , Toast.LENGTH_SHORT).show();
        ParseUser.logInInBackground(details[0], details[1], new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if( e==null && user!= null ){
                    Toast.makeText(getApplicationContext() , "Login Successful" , Toast.LENGTH_SHORT).show() ;
                    Intent intent = new Intent(getApplicationContext() , usersList.class ) ;
                    startActivity(intent) ;
                }
                else{
                    Toast.makeText(getApplicationContext() ,"Username or Passsword is Incorrect!" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if( keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN )  {
            logUp(v); ;
        }
        return false;
    }
}