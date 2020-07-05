package com.example.parseserverstarterproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent()) ;

/*        ParseObject object = new ParseObject("Score") ;
        object.put("Username" , "Sanjay");
        object.put("Score" , 90);

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i("Parsing" ,  "Successful");
                }
                else{
                    Log.i("Parsing" , "Unsuccessful") ;
                }
            }
        });

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Score");
        query.getInBackground("jIQPo8d4Ue", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e==null && object!= null){
                    Log.i("Username" , object.getString("Username")) ;
                    Log.i("Score" , Integer.toString(object.getInt("Score"))) ;
                }
                else{
                    Log.i("Grabbing" , "Unsuccessful");
                }
            }
        });
 */

        // query.whereGreaterThan("Score" , 50) ; This Returns only Objects with Score > 50 but we have to do findInBackground to get the list
/*
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Score") ;
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for (ParseObject object : objects){
                     if(object.getInt("Score") > 50){
                         object.put("Score" , object.getInt("Score")+20) ;
                         Log.i("Username" , object.getString("Username")) ;
                         Log.i("Score" , Integer.toString(object.getInt("Score"))) ;
                         object.saveInBackground() ;
                     }
                }
            }
        }) ;
 */

/*      User Login Part
        ParseUser user = new ParseUser() ;
        user.setUsername("shashwat");
        user.setPassword("password");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null ){
                    Log.i("Signed in" , "Successful") ;
                }
                else{
                    Log.i("Sign In " , "Unsuccessful") ;
                }
            }
        });
 */

/*
        ParseUser.logInInBackground("shashwat", "password", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null && user!=null){
                    Log.i("Login Successful" , user.getUsername() ) ;
                }
                else{
                    e.printStackTrace();
                    Log.i("Login Unsuccessful" , "Invalid User Name") ;
                }
            }
        });
*/

        ParseUser.logOut();
        if (ParseUser.getCurrentUser() != null){
            Log.i("User Exsits" , ParseUser.getCurrentUser().getUsername()) ;
        }
        else{
            Log.i("No Current" , "User") ;
        }
    }
}