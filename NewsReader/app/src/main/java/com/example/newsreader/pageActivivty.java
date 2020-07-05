package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class pageActivivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_activivty);

        WebView webView = findViewById(R.id.webView) ;
        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent = getIntent() ;
        int index = intent.getIntExtra("index" , -1) ;

        if(index!= -1)
        webView.loadUrl(MainActivity.titleURL.get(index));
        else{
            webView.loadData("<html><body><h1>ERROR 404</h1><p>URL Not Found</p></body></html>" , "text/html" , "UTF-8");
        }
    }
}