package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class usersList extends AppCompatActivity {

    ArrayList<String> usersList ;
    ArrayAdapter<String> adapter ;
    ListView listView  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        setTitle("Users List");


        listView = findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        usersList = new ArrayList<String>() ;


        adapter = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_checked , usersList) ;

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view  ;
                if( checkedTextView.isChecked() ) {
                    ParseUser.getCurrentUser().add("isFollowing" , usersList.get(i));
                    Toast.makeText(usersList.this ,usersList.get(i)+" Followed" , Toast.LENGTH_SHORT).show();
                }
                else{
                    ParseUser.getCurrentUser().getList("isFollowing").remove(usersList.get(i)) ;

                    List tempList = ParseUser.getCurrentUser().getList("isFollowing") ;

                    ParseUser.getCurrentUser().remove("isFollowing");

                    ParseUser.getCurrentUser().put("isFollowing" , tempList);

                    Toast.makeText(usersList.this,  usersList.get(i)+" Unfollowed", Toast.LENGTH_SHORT).show();
                }

                ParseUser.getCurrentUser().saveInBackground() ;
            }
        });

        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();

        parseUserParseQuery.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername() );

        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects != null && e == null) {
                    for (ParseUser pu : objects) {
                        usersList.add(pu.getUsername());
                    }

                    adapter.notifyDataSetChanged();

                    for (String u : usersList) {
                        if ( ParseUser.getCurrentUser().getList("isFollowing") != null && ParseUser.getCurrentUser().getList("isFollowing").contains(u)) {
                            Log.i("Ckecking For" , u) ;
                            Log.i("Index " , Integer.toString(usersList.indexOf(u)) ) ;
                            listView.setItemChecked(usersList.indexOf(u) , true);
                        }
                    }
                }
            }
        }) ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this) ;
        menuInflater.inflate(R.menu.userslist_menu , menu) ;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if( item.getItemId() == R.id.tweet ){
            Intent i = new Intent(this , tweetPage.class) ;
            startActivity(i);
        }
        else if( item.getItemId() == R.id.myfeed ){
            Intent i = new Intent(this , user_feeds.class);
            startActivity(i);
        }
        else if( item.getItemId() == R.id.smalltweet ){

            final EditText editText = new EditText(this) ;

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this) ;
            alertDialog.setTitle("Tweet");
            alertDialog.setView(editText);

            alertDialog.setPositiveButton("Tweet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ParseObject parseObject = new ParseObject("Tweets") ;
                    parseObject.put("username" , ParseUser.getCurrentUser().getUsername());
                    parseObject.put( "tweet" , editText.getText().toString() );
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if( e == null ){
                                Toast.makeText(getApplicationContext() , "Tweeted Successfully" , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext() , "Error Tweeting" , Toast.LENGTH_SHORT ).show();
                            }
                        }
                    });
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext() , "Tweet Cancelled" , Toast.LENGTH_SHORT).show() ;
                }
            }) ;
            alertDialog.show() ;
        }
        else if( item.getItemId() == R.id.logout){
            ParseUser.logOut();
            Log.i("User" , "LoggedOut");
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}