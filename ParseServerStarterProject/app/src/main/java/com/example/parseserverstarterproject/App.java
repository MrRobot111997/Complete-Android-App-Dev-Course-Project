package com.example.parseserverstarterproject;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myappID")
                // if defined
                .clientKey("")                  //Client Key Removed to For Safe KEeping Just in case .
                .server("http://18.224.136.254/parse/")
                .build()
        );
        //* perform your parse query

/*        ParseObject object = new ParseObject("ExampleObject") ;
        object.put("myNumber" , "123");
        object.put("myString" , "rob");

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if( ex == null){
                    Log.i("Parse Result" , "Successful") ;
                }
                else{
                    ex.printStackTrace();
                    Log.i("Parse Reuslt" , "Unsuccessful" ) ;
                }
            }
        });
*/
    }
}
