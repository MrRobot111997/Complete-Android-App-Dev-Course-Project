package com.example.numbershapeschecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void checkNumber(View view) {
        EditText eT = (EditText) findViewById(R.id.editTextTextPersonName);
        TextView tV = (TextView) findViewById(R.id.textView3);

        if (eT.getText().toString().equals("")) {
            Toast.makeText(this, "The Input Must Be Valid", Toast.LENGTH_SHORT).show();
        } else {
            int num = Integer.parseInt(eT.getText().toString());

            int i = 0, incrementer = 1;
            boolean sq = false, tri = false;

            while (i <= num) {
                if (i == num) {
                    tri = true;
                    break;
                }
                i += incrementer;
                incrementer++;
            }

            if ( Math.sqrt(num) % (int) Math.sqrt(num) == 0.0 ) {
                sq = true;
            }

            if ( sq && tri ) {
                tV.setText("It's Both Square as Well as Triangular No.");
            } else if (sq) {
                tV.setText("It's Just Square No.");
            } else if (tri) {
                tV.setText("It's just Triangular No.");
            } else {
                tV.setText("The No. is Neither");
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}