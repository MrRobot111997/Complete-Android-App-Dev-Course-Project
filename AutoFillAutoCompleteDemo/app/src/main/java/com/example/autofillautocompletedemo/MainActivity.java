package com.example.autofillautocompletedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView) ;

        String[] friends = {"Akshit", "Deepak", "Vaibahv", "Kunal", "Kushagra", "Karmanya", "Ravi", "Sivansh", "Shyam Ji"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 , friends) ;

        autoCompleteTextView.setAdapter(adapter);
    }
}