package com.example.jsondemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView tv ;

    class WeatherDownloader extends AsyncTask<String ,Void , String>{

        @Override
        protected String doInBackground(String... urls) {

            URL url ;
            HttpURLConnection conn ;
            InputStream in ;
            try {

                Log.i("URL" , urls[0]) ;
                url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                in = conn.getInputStream() ;
                InputStreamReader reader = new InputStreamReader(in) ;
                BufferedReader b_reader = new BufferedReader(reader) ;
                StringBuffer sb = new StringBuffer() ;
                String weather = ""  ;
                while (( weather = b_reader.readLine() ) != null){
                    sb.append(weather) ;
                }
                weather = sb.toString() ;
                return weather ;

            } catch (Exception e) {
                e.printStackTrace();
                return "Failure" ;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject json = new JSONObject(s);

                String weatherInfo = json.getString("weather");

                tv.setText(weatherInfo);

                Log.i("JSON" , weatherInfo) ;

                JSONArray arr = new JSONArray(weatherInfo) ;

                for(int i=0;i<arr.length() ;i++){
                    JSONObject jsonPart = arr.getJSONObject(i) ;

                    Log.i("main" , jsonPart.getString("main")) ;
                    Log.i("description" , jsonPart.getString("description")) ;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String weatherJSON = "no" ;
        tv = findViewById(R.id.tv) ;
        WeatherDownloader wd = new WeatherDownloader() ;
        try {
            weatherJSON = wd.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=439d4b804bc8187953eb36d2a8c26a02").get() ;
            Log.i("Weather" , weatherJSON ) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv.setText(weatherJSON);
    }
}