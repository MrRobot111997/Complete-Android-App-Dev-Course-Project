package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView ;
    Intent intent;
    ArrayList<String> arrayList ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list) ;

        arrayList  = new ArrayList<String>() ;
        arrayList.add("Akshit") ;
        arrayList.add("Deepak");
        arrayList.add("Karmanya") ;
        arrayList.add("Kunal") ;
        arrayList.add("Kushagra");
        arrayList.add("Rahul") ;
        arrayList.add("Ravi");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,arrayList) ;
        listView.setAdapter(arrayAdapter);

        intent=  new Intent (getApplicationContext() , secondActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("friend" , arrayList.get(position)) ;
                startActivity(intent);
            }
        });

    }
}