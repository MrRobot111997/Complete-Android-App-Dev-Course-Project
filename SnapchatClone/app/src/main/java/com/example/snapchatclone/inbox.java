package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.content.Intent;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class inbox extends AppCompatActivity {

    ArrayList<String> emails ;
    ArrayList<DataSnapshot> snaps ;
    ArrayAdapter<String> adapter ;
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        listView = findViewById(R.id.imboxListView) ;
        emails = new ArrayList<>() ;
        snaps = new ArrayList<DataSnapshot>() ;
        adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 , emails) ;

        listView.setAdapter(adapter);

        Log.i("Reached " , FirebaseAuth.getInstance().getCurrentUser().getUid()) ;

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snap").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    emails.add(snapshot.child("from").getValue().toString());
                    Log.i("value:", snapshot.child("from").getValue().toString());
                    Log.i("SnapShots Values:", snaps.toString());

                    adapter.notifyDataSetChanged();
                    snaps.add(snapshot);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext() , "No New Messages" ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = 0 ;
                for(DataSnapshot snapshot1 : snaps ){
                    if( snapshot1.getKey().equals(snapshot.getKey()) ){
                        snaps.remove(index);
                        emails.remove(index);
                    }
                    index++ ;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataSnapshot dataSnapshot = snaps.get(i) ;
                Intent intent = new Intent(getApplicationContext() , snapViewer.class) ;
                intent.putExtra("imageURL" , dataSnapshot.child("imageURL").getValue().toString()) ;
                intent.putExtra("message" , dataSnapshot.child("message").getValue().toString() ) ;
                intent.putExtra("from" , dataSnapshot.child("from").getValue().toString()) ;
                intent.putExtra("snapKey" , dataSnapshot.getKey().toString() ) ;
                intent.putExtra("imageName" , dataSnapshot.child("imageName").getValue().toString()) ;
                Log.i("snapKey" , dataSnapshot.getKey().toString() ) ;
                startActivity(intent) ;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this) ;
        menuInflater.inflate(R.menu.inboxmenu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if( item.getItemId() == R.id.send ){
            Intent i = new Intent(this , sendsnap.class);
            startActivity(i) ;
        }
        else if( item.getItemId() == R.id.logout ) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}