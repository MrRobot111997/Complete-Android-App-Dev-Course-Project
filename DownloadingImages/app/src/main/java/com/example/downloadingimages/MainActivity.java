package com.example.downloadingimages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView imageView ;
    public void downloadView(View view){
        Downloader image = new Downloader();
        Bitmap img ;

        try{
            img = image.execute("https://wallpapercave.com/wp/wp2740983.jpg").get() ;
            imageView.setImageBitmap(img);
        }catch (Exception e){
            e.printStackTrace();
            imageView.setBackgroundResource(R.color.black);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView) ;


    }

    public class Downloader extends AsyncTask<String , Void , Bitmap>{

        URL url ;
        HttpURLConnection conn ;

        @Override
        protected Bitmap doInBackground(String... urls) {

            try{
                url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection() ;
                conn.connect();
                InputStream in = conn.getInputStream() ;
                Bitmap bitmap = BitmapFactory.decodeStream(in) ;
                return bitmap ;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

}