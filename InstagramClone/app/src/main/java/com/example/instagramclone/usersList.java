package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class usersList extends AppCompatActivity  {

    ListView listView ;
    ListAdapter adapter ;
    ArrayList<String> users ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        setTitle("Instagram");

        listView = findViewById(R.id.usersList) ;
        users = new ArrayList<String>() ;
        adapter = new ArrayAdapter<String>( getApplicationContext() , android.R.layout.simple_list_item_1 , users  ) ;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext() , user_feed.class) ;
                intent.putExtra("username" , users.get(position) ) ;
                startActivity(intent);
            }
        });


        ParseQuery<ParseUser> query = ParseUser.getQuery() ;

        query.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername()) ;
        query.addAscendingOrder("username") ;
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                for( ParseUser object : objects ){
                    users.add(object.getUsername()) ;
                }
                listView.setAdapter(adapter) ;
                Log.i("Users" , users.toString()) ;
            }
        });
    }

    public void addPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI) ;
        startActivityForResult(intent , 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode==1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions.length>0 ){
            addPhoto() ;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == RESULT_OK && data != null){
            Uri selectedImages = data.getData() ;
            try {
                   Bitmap bitmap = MediaStore.Images.Media.getBitmap( this.getContentResolver() , selectedImages ) ;
                   Log.i("Image Selected" , "Good Work") ;

                ByteArrayOutputStream stream = new ByteArrayOutputStream() ;

                bitmap.compress(Bitmap.CompressFormat.PNG , 100 , stream) ;

                byte[] bytesArray = stream.toByteArray() ;

                ParseFile file = new ParseFile("image.png" , bytesArray) ;

                ParseObject object = new ParseObject( "Images" ) ;

                object.put("image" , file) ;

                object.put("username" , ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(getApplicationContext() , "Image Shared" , Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext() , "Image Upload Failed" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater() ;
        menuInflater.inflate(R.menu.list_menu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.add ){
            if ( checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
            }
            else {
                addPhoto();
            }
        }
        if(item.getItemId() == R.id.logout){
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext() , MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}