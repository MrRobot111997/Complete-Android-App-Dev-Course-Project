package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> title = new ArrayList<>() ;
    static ArrayList<String> titleURL = new ArrayList<>() ;
    ArrayAdapter<String> adapter ;
    SQLiteDatabase sqLiteDatabase ;

    public class DownloadNews extends AsyncTask<String , Void , String>{

        @Override
        protected String doInBackground(String... urls) {

            URL url ;
            HttpURLConnection conn ;

            try {

                url = new URL(urls[0]) ;
                conn = (HttpURLConnection) url.openConnection() ;
                conn.connect();

                BufferedReader reader = new BufferedReader( new InputStreamReader( conn.getInputStream() )) ;
                StringBuffer str = new StringBuffer() ;
                String code = "" ;

                while( (code = reader.readLine()) != null ) {
                    str.append(code);
                }

                code = str.toString() ;
                Log.i("Data" , code) ;
                    JSONArray codes = new JSONArray(code) ;

                    sqLiteDatabase.execSQL("DELETE FROM websites");

                    for (int i=0;i< 20 ; i++){
                        try{
                            url = new URL("https://hacker-news.firebaseio.com/v0/item/"+codes.get(i)+".json?print=pretty" ) ;
                            conn = (HttpURLConnection) url.openConnection();
                            conn.connect();

                            reader = new BufferedReader( new InputStreamReader(conn.getInputStream())) ;
                            str = new StringBuffer() ;
                            String codeContent = "" ;

                            while( ( codeContent = reader.readLine()) != null ){
                                str.append(codeContent) ;
                            }

                            JSONObject jsonContent = new JSONObject(str.toString()) ;

                            Log.i("JSON" , jsonContent.toString() ) ;

                            String title = jsonContent.getString("title") ;
                            String webURL = jsonContent.getString("url") ;

                            String sql = "INSERT INTO  websites ( title , url ) VALUES ( ? , ? )" ;
                            SQLiteStatement statement = sqLiteDatabase.compileStatement(sql) ;
                            statement.bindString(1  , title);
                            statement.bindString(2 , webURL);

                            statement.execute();

                        }catch(Exception e){
                            e.printStackTrace();
                            Log.i("Not Working" , codes.get(i).toString()) ;
                        }
                    }
                    updateView();

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



        }
    }


    public void updateView(){

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM websites" , null) ;

        int titleIndex = c.getColumnIndex("title") ;
        int urlIndex = c.getColumnIndex("url") ;

        if(c.moveToFirst()){
            title.clear();
            titleURL.clear();
        }

        do{
            title.add( c.getString(titleIndex)) ;
            titleURL.add(c.getString(urlIndex)) ;
        }while(c.moveToNext()) ;

        adapter.notifyDataSetChanged();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView) ;
        adapter = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1 , title) ;
        listView.setAdapter(adapter);

        sqLiteDatabase = this.openOrCreateDatabase("Websites" , MODE_PRIVATE , null) ;
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS websites (id INTEGER PRIMARY KEY , title VARCHAR , url VARCHAR )");

        try {
            DownloadNews downloadNews = new DownloadNews();
            downloadNews.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get() ;

        }catch (Exception e){
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext() , pageActivivty.class ) ;
                intent.putExtra("index" , position) ;
                startActivity(intent);
            }
        });
    }

}