package com.example.downloadingwebcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String ,Void , String>{

        @Override
        protected String doInBackground(String... urls) {

            Log.i("URL = " , urls[0]);
            String result = "" ;
            URL url ;
            HttpURLConnection conn ;

            try {
                url = new URL(urls[0]) ;
                conn = (HttpURLConnection) url.openConnection() ;
                InputStream in = conn.getInputStream() ;
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read() ;

                while(data != -1){

                    char letter = (char) data ;
                    result = result+letter ;
                    data = reader.read() ;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Failure" ;
            }

            return result;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.text) ;

        DownloadTask download = new DownloadTask();
        String out = null ;
        try {
            out = download.execute("https://zappycode.com").get();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        textView.setText(out);
        Log.i("Output" , out ) ;
    }
}