package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chooseuser extends AppCompatActivity {

    ArrayList<String> arrayList ;
    ArrayList<String> uidList ;
    ArrayAdapter<String> adapter ;
    ListView listView ;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseuser);

        listView = findViewById(R.id.chooseUserListView) ;
        arrayList = new ArrayList<String>() ;
        uidList = new ArrayList<String>() ;
        adapter = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1 , arrayList) ;
        listView.setAdapter(adapter);

/*
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for( DataSnapshot key : snapshot.getChildren() ) {
                    String email = key.child("email").getValue().toString();
                    arrayList.add(email);
                    Log.i("Got Data :" , email  ) ;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;
*/
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                uidList.add(snapshot.getKey()) ;
                Log.i("UID Array" , uidList.toString() );
                arrayList.add(snapshot.child("email").getValue().toString()) ;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getIntent() ;
                HashMap<String , String > map = new HashMap<String, String>() ;
                map.put("from" , FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()) ;
                map.put("imageURL" , intent.getStringExtra("imageURL")) ;
                map.put("imageName" , intent.getStringExtra("imageName")) ;
                map.put("message" , intent.getStringExtra("message")) ;
                FirebaseDatabase.getInstance().getReference().child("users").child( uidList.get(i) ).child("snap").push().setValue(map) ;

                Intent intent1 = new Intent(getApplicationContext() , inbox.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                startActivity(intent1);
            }
        });
    }


}