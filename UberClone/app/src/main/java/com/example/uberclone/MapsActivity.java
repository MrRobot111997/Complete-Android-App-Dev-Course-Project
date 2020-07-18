package com.example.uberclone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager manager ;
    LocationListener locationListener ;
    LatLng latLng ;

    boolean activeRide = false ;
    Button button ;
    ParseObject requestObject ;

    TextView tv ;

    Handler handler = new Handler() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = findViewById(R.id.acceptButton) ;
        tv = findViewById(R.id.textView3) ;
        Log.i("Logged in as" , ParseUser.getCurrentUser().toString()) ;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        manager = (LocationManager) getSystemService(LOCATION_SERVICE) ;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocation(location);
            }
        } ;

        Log.i("Map" , "Started" ) ;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else{
            startListening();
        }
    }

    public void updateLocation(Location last){

        if( !activeRide ) {

            latLng = new LatLng(last.getLatitude(), last.getLongitude());
            Log.i("LatLng", latLng.toString());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnown = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnown != null) {
                updateLocation(lastKnown);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(  grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                startListening();
        }
        else{
            ActivityCompat.requestPermissions(this , new String[]{ Manifest.permission.ACCESS_FINE_LOCATION } , 1);
        }
    }

    public void startRide(View view){
        if( !activeRide ){
            button.setText("Cancel Ride") ;
            activeRide = true ;
            requestObject = new ParseObject("Requests") ;
            requestObject.put("username" , ParseUser.getCurrentUser().getUsername()) ;
            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(latLng.latitude , latLng.longitude) ;
            requestObject.put("location" , parseGeoPoint );
            Log.i("Saving Location" , latLng.toString() ) ;
            requestObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if( e == null){
                        Toast.makeText(MapsActivity.this, "Rider Starting...", Toast.LENGTH_SHORT).show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkForUpdates();
                            }
                        } , 2000);
                    }
                    else{
                        e.printStackTrace();
                        Toast.makeText(MapsActivity.this, "No Ride Available ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            cancelRide();
        }
    }

    public void checkForUpdates(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Requests");
        query.whereEqualTo("username" , ParseUser.getCurrentUser().getUsername()) ;
        query.whereExists("driverUsername") ;
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if( e==null ){
                    if( objects.size()>0 ){
                        Log.i("Got" , "Driver") ;
                        button.setVisibility(View.INVISIBLE);

                            ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery() ;
                            parseUserParseQuery.whereEqualTo("username" , objects.get(0).getString("driverUsername")).findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {
                                    if (e == null && objects.size() > 0) {

                                        ParseGeoPoint driverGeoPoint = objects.get(0).getParseGeoPoint("location");
                                        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                            if (location != null) {

                                                ParseGeoPoint userGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                                                Double dis = driverGeoPoint.distanceInKilometersTo(userGeoPoint);

                                                dis = (double) Math.round(dis * 10) / 10;

                                                if (dis < 0.01) {

                                                    cancelRide();
                                                    button.setVisibility(View.VISIBLE);
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            checkForUpdates();
                                                        }
                                                    } , 5000);
                                                    Toast.makeText(MapsActivity.this , "Driver is Here" , Toast.LENGTH_SHORT).show();

                                                } else {

                                                    Log.i("Got" , "Location of Driver") ;
                                                    tv.setText("Uber is " + dis + " K.Ms Away");

                                                    ArrayList<Marker> markers = new ArrayList<Marker>();
                                                    markers.clear();
                                                    mMap.clear();
                                                    markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(driverGeoPoint.getLatitude(), driverGeoPoint.getLatitude())).title("Driver's Location")));
                                                    markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(userGeoPoint.getLatitude(), userGeoPoint.getLatitude())).title("your Location")));

                                                    final LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                                    for (Marker mark : markers) {
                                                        builder.include(mark.getPosition());
                                                    }
                                                    LatLngBounds bounds = builder.build();

                                                    int padding = 50;
                                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                                                    mMap.animateCamera(cu);

                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            checkForUpdates();
                                                        }
                                                    } , 2000);
                                                }
                                            }
                                        } else {
                                            Toast.makeText(MapsActivity.this, "Not Updateable", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                    }
                }

            }
        });
    }

    public void cancelRide(){
        button.setText("Start Ride");
        activeRide = false ;
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Requests") ;
        parseQuery.whereEqualTo("username" , ParseUser.getCurrentUser());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if( objects.size()>0 )
                        for (ParseObject object : objects) {
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(MapsActivity.this, "Ride Cancelled...", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MapsActivity.this, "Can't Cancel Ride", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    else
                        Toast.makeText(MapsActivity.this, "Unable to Retrieve Data", Toast.LENGTH_SHORT).show();
                } else {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Can't Cancel Ride", Toast.LENGTH_SHORT).show();
                }
            }
        }) ;
    }

    public void logOut(View view){
        ParseUser.logOut();
        Intent i = new Intent(this , MainActivity.class);
        startActivity( i );
    }
}