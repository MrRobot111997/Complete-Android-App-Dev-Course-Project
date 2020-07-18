package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class user_feeds extends AppCompatActivity {

    ArrayList<String> usersTweets ;
    ListView listView ;
    ArrayAdapter<String> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feeds);

        setTitle(ParseUser.getCurrentUser().getUsername() + "'s Feed" );

        usersTweets = new ArrayList<>() ;
        listView = findViewById(R.id.feedListView) ;
        adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 , usersTweets) ;


        ParseQuery<ParseObject> tweets = ParseQuery.getQuery("Tweets") ;

        tweets.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername()) ;

        listView.setAdapter(adapter);

        tweets.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if( objects!=null && e == null ){
                    int i = 0 ;
                    for( ParseObject object : objects ){
                        if( ParseUser.getCurrentUser().getList("isFollowing") != null && ParseUser.getCurrentUser().getList("isFollowing").contains( object.getString("username") ) ) {
                            usersTweets.add(object.getString("tweet"));
                            i = 1 ;
                        }
                    }
                    if(i==1) {
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext() , "Tweets Downloaded" , Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Retriving Tweets Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}