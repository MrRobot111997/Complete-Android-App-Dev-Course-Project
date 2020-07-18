package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class sendsnap extends AppCompatActivity {

    ImageView selectPic  ;
    EditText message ;

    String imageName = UUID.randomUUID().toString() + ".jpg" ;
    Bitmap bitmap ;
    String imageURL ="LOL" ;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode==1  ){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions.length > 0  ){
                Log.i("Permission" , "Aquired") ;
            }
            else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendsnap);

        selectPic = findViewById(R.id.imageView) ;
        message = findViewById(R.id.messageEditText) ;

        if( checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
        }
    }

    public void selectPicture(View view){
        if( checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
        }
        else {
            Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(getImage, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == 1 && resultCode == RESULT_OK && data != null ){
            Uri selectedImage = data.getData() ;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , selectedImage) ;
                selectPic.setImageBitmap(bitmap) ;
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(this, "There was an Error Importing the Picture!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendToUsers(View view){
        final String usermessage = message.getText().toString() ;
        selectPic.setDrawingCacheEnabled(true);
        selectPic.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
        bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , baos) ;
        byte[] data = baos.toByteArray() ;

        FirebaseStorage.getInstance().getReference().child("Images").child(imageName).putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                final Intent i = new Intent(getApplicationContext() , chooseuser.class) ;
                i.putExtra("imageName" , imageName) ;
                i.putExtra("message" , usermessage) ;

                FirebaseStorage.getInstance().getReference().child("Images/"+imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageURL = uri.toString() ;
                        Log.i("Image Name:" , imageName) ;
                        Log.i("Uploaded to:" , imageURL) ;

                        i.putExtra("imageURL" , imageURL ) ;
                        startActivity(i) ;
                    }
                });

                Log.i("Uploaded to:" , imageURL) ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(sendsnap.this, "There was an error Uploading the Pic!", Toast.LENGTH_SHORT).show();
            }
        }) ;
    }
}