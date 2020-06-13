package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView text ;


    class WeatherSync extends AsyncTask<String ,Void ,String>{

        @Override
        protected String doInBackground(String... urls ) {
            URL url ;
            HttpURLConnection conn ;

            try {
                Log.i("URL" , urls[0]) ;
                url = new URL(urls[0]) ;
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream in = conn.getInputStream() ;

                BufferedReader reader = new BufferedReader(new InputStreamReader(in)) ;
                StringBuffer weather =  new StringBuffer() ;
                String str = "" ;
                while( (str = reader.readLine())!=null ){
                    weather.append(str) ;
                }

                return weather.toString() ;

            }catch (Exception e){
                e.printStackTrace();
                text.setText("Invalid City Name");
                return "Failed to Retrieve Data" ;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject json = new JSONObject(s) ;

                String weatherInfo = json.getString("weather") ;

                JSONArray arr = new JSONArray(weatherInfo) ;

                String outputText = "Weather :" ;

                for(int i =0 ;i < arr.length() ; i++){

                    if(i!=0){
                        i=1 ;weatherInfo += "\n" ;
                    }

                    JSONObject part = arr.getJSONObject(i) ;

                    String main = part.getString("main");
                    String desc = part.getString("description");

                    if(!main.equals("") && !desc.equals("")){
                        outputText += (main+" ("+desc+")") ;
                    }
                    text.setText(outputText);
                }

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void getData(View view){
        EditText city = findViewById(R.id.cityName) ;
        WeatherSync ws = new WeatherSync();
        try {
            ws.execute("https://api.openweathermap.org/data/2.5/weather?q=" + city.getText().toString().toLowerCase() + "&appid=c4b19b2fc65782a8b95b1818212fd86c").get();
        }catch(Exception e){
            e.printStackTrace();
            text.setText("Something Went Wrong\nContact Developer");
        }
        text.setVisibility(View.VISIBLE);
        InputMethodManager mng = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE) ;
        mng.hideSoftInputFromWindow(city.getWindowToken(), 0) ;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        text = findViewById(R.id.textView2)  ;
        text.setVisibility(View.INVISIBLE);
    }
}