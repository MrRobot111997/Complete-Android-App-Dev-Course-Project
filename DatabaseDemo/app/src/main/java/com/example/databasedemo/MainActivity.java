package com.example.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS user (name VARCHAR , age INT(2))");
 //           sqLiteDatabase.execSQL("INSERT INTO user(name ,age) VALUES ('SHASHWAT' , 23)");
 //           sqLiteDatabase.execSQL("INSERT INTO user(name,age) VALUES('Deepak' , 22)");

            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM user" , null);
            int nameIndex = c.getColumnIndex("name") ;
            int ageIndex = c.getColumnIndex("age") ;
            c.moveToFirst() ;

            while(c!=null){
                Log.i("Name" , c.getString(nameIndex)) ;
                Log.i("Age" , Integer.toString(c.getInt(ageIndex)));
                c.moveToNext() ;
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}