package com.example.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notesList ;
    static ArrayAdapter<String> arrayAdapter ;
    static ListView notes ;
    static SharedPreferences sharedPreferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesList = new ArrayList<String>() ;
        notes = findViewById(R.id.notes) ;
        sharedPreferences =  this.getSharedPreferences("com.example.simplenotes" , Context.MODE_PRIVATE) ;

        try {
            notesList = (ArrayList<String>) ObjectSerializer.deserialize( sharedPreferences.getString("notes" , ObjectSerializer.serialize( new ArrayList<String>() )));

        } catch (Exception e) {
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 , notesList) ;
        notes.setAdapter(arrayAdapter);

        notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent( getApplicationContext(), showNotes.class) ;
                intent.putExtra("note" , notesList.get(position) ) ;
                intent.putExtra("index" , position ) ;
                startActivity(intent);
            }
        });

        notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                Log.i("Pressed" , "Long") ;
                new AlertDialog.Builder( MainActivity.this )
                        .setTitle("Do You Really Want to Delete the Note ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesList.remove(position) ;
                                try {
                                    sharedPreferences.edit().putString("notes" , ObjectSerializer.serialize(notesList) ).apply();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this) ;
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.newNote: Intent intent = new Intent( this , takeNotes.class) ;
                               startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void newNote(View view){

        Intent intent = new Intent(getApplicationContext() , takeNotes.class) ;
        startActivity(intent);

    }
}