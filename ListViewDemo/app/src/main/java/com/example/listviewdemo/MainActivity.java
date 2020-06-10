package com.example.listviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        final ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Shashwat");
        arrayList.add("Deepak");
        arrayList.add("Kunal");
        arrayList.add("Kushagra");
        arrayList.add("Karmanya");
        arrayList.add("Appu");
        arrayList.add("Akshit");
        arrayList.add("Ravi");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , arrayList) ;

        listView.setAdapter(arrayAdapter) ;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText( getApplicationContext(), arrayList.get(position) , Toast.LENGTH_SHORT).show();
            }
        });

    }
}