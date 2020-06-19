package com.example.favoritelocations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ListView listView ;
    static ArrayList<String> addList ;
    static ArrayAdapter<String> adapter ;
    static ArrayList<String> lat ;
    static ArrayList<String> lon ;

    static ObjectSerializer obj ;
    static SharedPreferences sharedPreferences ;

    public void newLoc(View view){

        Intent intent = new Intent(this , MapsActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.favoritelocations" , MODE_PRIVATE) ;

        listView = findViewById(R.id.list) ;
        addList = new ArrayList<String>() ;
        lat = new ArrayList<String>() ;
        lon =  new ArrayList<String>() ;

        try {
            addList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("locations", ObjectSerializer.serialize(new ArrayList<String>())));
            lat = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lat" , ObjectSerializer.serialize(new ArrayList<String>()))) ;
            lon = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("long" , ObjectSerializer.serialize(new ArrayList<String>()))) ;
        } catch (Exception e){
            e.printStackTrace();
        }
        adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1 , addList) ;

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext() , MapsActivity.class) ;
                intent.putExtra("index" , position) ;
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                addList.remove(position) ;
                lat.remove(position) ;
                lon.remove(position) ;
                try {
                    sharedPreferences.edit().putString( "locations" , ObjectSerializer.serialize( addList )).apply(); ;
                    sharedPreferences.edit().putString("lat" , ObjectSerializer.serialize(lat)).apply();
                    sharedPreferences.edit().putString("long" , ObjectSerializer.serialize(lon)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();

                return false;
            }
        }) ;

    }
}