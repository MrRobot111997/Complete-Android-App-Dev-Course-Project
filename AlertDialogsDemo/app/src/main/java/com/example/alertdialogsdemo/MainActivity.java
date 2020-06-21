package com.example.alertdialogsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String choice  ;
    TextView tv  ;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView) ;
        sharedPreferences = getSharedPreferences("com.example.alertdialogsdemo;" , Context.MODE_PRIVATE) ;

        if(sharedPreferences.getBoolean("first" , true)){
            userChoice();
            sharedPreferences.edit().putBoolean("first" , false ).apply();
        }
        else{
            choice = sharedPreferences.getString("choice" , "No Choice Made Yet");
            setLanguage();
        }
    }

    public void userChoice(){

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Which Language Do you want to Choose")
                .setPositiveButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = "English" ;
                        setLanguage();
                    }
                })
                .setNegativeButton("Spanish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = "Spanish" ;
                        setLanguage();
                    }
                })
                .show() ;

    }

    public void setLanguage(){
        Log.i("Choice Made" , choice);
        tv.setText(choice);
        sharedPreferences.edit().putString("choice" , choice).apply() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.language:userChoice();
                setLanguage();
                break;
            case R.id.reset: sharedPreferences.edit().putBoolean("first",true).apply();
                    finish();
                    System.exit(0) ;
                    break ;
            default:
                Toast.makeText(MainActivity.this , "No Choice Made" ,Toast.LENGTH_SHORT).show();
        }
        tv.setText(choice);
        return super.onOptionsItemSelected(item);
    }
}

