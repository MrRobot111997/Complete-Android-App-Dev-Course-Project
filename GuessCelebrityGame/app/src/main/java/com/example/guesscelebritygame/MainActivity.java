package com.example.guesscelebritygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView textView ;

    public class ContentDownloader extends AsyncTask<String ,Void ,String>{

        @Override
        protected String doInBackground(String... urls) {

            Log.i("URL" , urls[0]) ;

            URL url ;
            HttpURLConnection conn ;
            String website = "" ;

            try {
                url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection() ;
                conn.connect();
                InputStream in = conn.getInputStream() ;
                InputStreamReader reader = new InputStreamReader(in) ;

                /*
                int data = reader.read();

                while(data != -1){

                    char letter = (char) data ;
                    textView.setText(website);
                    website += letter ;
                    data = reader.read();

                }

                 */
                BufferedReader breader = new BufferedReader(reader);
                StringBuffer sb = new StringBuffer();
                String str;
                while((str = breader.readLine())!= null){
                    sb.append(str);
                }
    //          website = str ;
                return sb.toString() ;

            } catch (Exception e) {
                e.printStackTrace();
                return "Failed Process" ;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String website = "";


        ContentDownloader cd = new ContentDownloader() ;
        try {
            website = cd.execute("https://www.imdb.com/list/ls052283250/").get() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String arr = "";
        String[] split = website.split("<div class=\"lister-item-image\">") ;
        List<String> splits = new ArrayList<>() ;
        Collections.addAll(splits,split) ;
        splits.remove(0) ;
        Pattern pattern = Pattern.compile(
                "img alt=\"(.*)\" src=\"(.*)\"" ) ;
        try {
            arr =  matcher.group(1) ;
        }catch (Exception e){
            arr = "No Match" ;
        }

        Log.i("Website" , arr) ;

        ListView listView = findViewById(R.id.listView) ;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this ,android.R.layout.simple_list_item_1 ,splits) ;
        listView.setAdapter(adapter);

    }
}