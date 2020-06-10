package com.example.numbertable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SeekBar number ;
    ArrayList<Integer> tableList;
    ListView table;

    public void changeTable(int progress){

        tableList = new ArrayList<Integer>() ;

        table = (ListView) findViewById(R.id.listView);

        for(int i=1;i<=10 ; i++){
            tableList.add(i*progress);
        }
        ArrayAdapter<Integer> tableAdapter = new ArrayAdapter<Integer>( getApplicationContext() , android.R.layout.simple_list_item_1 , tableList);
        table.setAdapter(tableAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        table = (ListView) findViewById(R.id.listView);

        number = (SeekBar) findViewById(R.id.number);

        number.setMax(20); number.setMin(1);

        changeTable(1);

        number.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                changeTable(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}