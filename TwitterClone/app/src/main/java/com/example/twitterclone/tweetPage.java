package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseEncoder;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class tweetPage extends AppCompatActivity {

    EditText editText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_page);

        setTitle("Tweeting...") ;

        editText = findViewById(R.id.editTextTextMultiLine) ;
    }

    public void tweet(View view){
        String tweet = editText.getText().toString() ;

        ParseObject parseObject = new ParseObject("Tweets") ;
        parseObject.put("username" , ParseUser.getCurrentUser().getUsername());
        parseObject.put( "tweet" , tweet );
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if( e == null ){
                    Toast.makeText(getApplicationContext() , "Tweeted Successfully" , Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext() , "Error Tweeting" , Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }
}