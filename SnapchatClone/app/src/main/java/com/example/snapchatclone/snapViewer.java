package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class snapViewer extends AppCompatActivity {

    TextView textView ;
    ImageView imageView ;

    String imageName ;
    String snapKey ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_viewer);

        textView = findViewById(R.id.messageTextView) ;
        imageView = findViewById(R.id.imageView2) ;

        Intent i = getIntent() ;
        String imageURL = i.getStringExtra("imageURL") ;
        String message = i.getStringExtra("message") ;
        snapKey = i.getStringExtra("snapKey") ;
        imageName = i.getStringExtra("imageName") ;

        ImageDownload imageDownload = new ImageDownload() ;
        Bitmap image  ;

        try {
            image = imageDownload.execute(imageURL).get() ;
            imageView.setImageBitmap(image);
            textView.setText(message);
        }catch( Exception e ){
            e.printStackTrace();
        }

    }

    public class ImageDownload extends AsyncTask<String , Void , Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            URL url ;
            HttpURLConnection conn ;
            try {
                url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection() ;

                InputStream inputStream = conn.getInputStream() ;
                Bitmap image = BitmapFactory.decodeStream(inputStream) ;

                return image ;
            }
            catch(Exception e ) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext() , "Image Download Failed" , Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        FirebaseStorage.getInstance().getReference().child("Images").child(imageName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Deleted" , "Storage") ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Delete Storage" , "Failed" ) ;
            }
        }) ;
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snap").child(snapKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Deleted" , "Database") ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Delete Database" , "Failed" ) ;
            }
        }) ;
        super.onBackPressed();
    }
}