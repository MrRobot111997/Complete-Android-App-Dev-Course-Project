package com.example.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class showNotes extends AppCompatActivity {
    int index ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

        TextView textView = findViewById(R.id.textView2) ;

        Intent intent = getIntent() ;

        textView.setText(intent.getStringExtra("note" ) );
        index = intent.getIntExtra("index" , -1) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater( this ) ;
        menuInflater.inflate(R.menu.note_menu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete :
                new AlertDialog.Builder(this)
                        .setTitle("Do You Really Want to Delete ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.notesList.remove(index) ;
                                MainActivity.arrayAdapter.notifyDataSetChanged();
                                try {
                                    MainActivity.sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notesList)).apply();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show() ;

                break;
            default:
                Toast.makeText(this, "There was an Error" , Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}