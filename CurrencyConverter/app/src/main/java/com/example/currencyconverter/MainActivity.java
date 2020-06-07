package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public void conversion(View view){

        EditText dEdit = (EditText) findViewById(R.id.editTextNumber);
        Double d = Double.parseDouble(dEdit.getText().toString()) ;

        TextView tv = (TextView) findViewById(R.id.textView2) ;

        Log.i("Dollor" , dEdit.getText().toString()) ;

        tv.setText(String.format("%.2f Dollar \n\t=\n Rs. %.2f", d, d * 70));

        Toast.makeText(this, String.format("%.2f Dollar = Rs. %.2f", d, d * 70), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}