package com.example.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class requestsView extends AppCompatActivity {

    ArrayList<Double> distance ;
    ArrayList<Double> latitude , longitude ;
    ArrayList<String> usernames ;
    ArrayAdapter<Double> adapter ;
    ListView listView ;
    int time = 0 ;
    LocationListener locationListener ;
    LocationManager manager ;
    LatLng latLng ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_view);

        setTitle("Active Ride Requests") ;

        distance = new ArrayList<Double>() ;
        latitude = new ArrayList<Double>() ;
        longitude = new ArrayList<Double>() ;
        usernames = new ArrayList<>() ;

        Log.i("Logged in as", ParseUser.getCurrentUser().getUsername()) ;

        listView = findViewById(R.id.requestListView ) ;

        adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1 , distance) ;

        listView.setAdapter(adapter) ;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                time++ ;
                ParseUser.getCurrentUser().put("location" , new ParseGeoPoint(location.getLatitude() , location.getLongitude()));
                updateListView(location);
                adapter.notifyDataSetChanged();
            }
        } ;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        Intent intent = new Intent(getApplicationContext(), driverMapView.class);

                        intent.putExtra("latitude", latitude.get(i));
                        intent.putExtra("longitude", longitude.get(i));
                        intent.putExtra("driverLat", location.getLatitude());
                        intent.putExtra("driverLong", location.getLongitude());
                        intent.putExtra("username" , usernames.get(i)) ;

                        startActivity(intent);
                }
            }
        });

        manager = (LocationManager) getSystemService(LOCATION_SERVICE) ;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
            time = 50  ;
            }
    }


    public void updateListView( Location location ){

        latitude.clear();
        longitude.clear();
        distance.clear();
        usernames.clear();

        Log.i("Time" , ""+time) ;

        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Requests") ;

        final ParseGeoPoint parseGeoPoint = new ParseGeoPoint( latLng.latitude , latLng.longitude ) ;

        parseQuery.findInBackground();

        parseQuery.whereNear( "location" , parseGeoPoint );
        parseQuery.setLimit( 10 ) ;

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if( e == null ){
                    if( objects.size()>0 ){
                        for( ParseObject object : objects ){

                            Log.i("info", object.toString());

                            Double dis = parseGeoPoint.distanceInKilometersTo((ParseGeoPoint) object.get("location") ) ;

                            latitude.add(((ParseGeoPoint) object.get("location")).getLatitude()) ;
                            longitude.add(((ParseGeoPoint) object.get("location")).getLongitude()) ;
                            usernames.add(object.get("username").toString()) ;

                            dis = (double) Math.round(dis * 10) / 10 ;
                            distance.add(dis) ;
                        }
                        Log.i("Lat" , latitude.toString()) ;
                        Log.i("Lng" , longitude.toString()) ;
                        Log.i("Username" , usernames.toString()) ;
                    }
                    else{
                        Toast.makeText(getApplicationContext() , "No Active Requests" , Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(requestsView.this, "Error Connecting to Server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(  grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
                Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                time = 100 ;
                updateListView(location);
            }
        }
    }

}