package com.example.wearlistdemo;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        ListView listView = findViewById(R.id.listView) ;

        final String[] arrayList = { "A" , "B" , "C" , "D" } ;

        ArrayAdapter arrayAdapter = new ArrayAdapter( getApplicationContext() , android.R.layout.simple_list_item_1 , arrayList) ;

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext() , "Friend:"+arrayList[position] , Toast.LENGTH_SHORT ).show();
            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }
}
