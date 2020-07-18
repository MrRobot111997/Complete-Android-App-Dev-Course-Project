package com.example.uberclone;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class driverMapView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<Marker> markers ;
    String username ;
    LatLng latLng ;
    Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.drivermap);
        mapFragment.getMapAsync(this);

        markers = new ArrayList<Marker>() ;
        button = findViewById(R.id.driverLogOut) ;
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

        Intent i = getIntent() ;
        username = i.getStringExtra("username") ;
        Log.i("Username" , username) ;
        latLng = new LatLng(i.getDoubleExtra( "driverLat"   , 0.0 ) , i.getDoubleExtra("driverLong" , 0.0)) ;
        markers.add( mMap.addMarker(new MarkerOptions().position( new LatLng(i.getDoubleExtra( "latitude"   , 0.0 ) , i.getDoubleExtra("longitude" , 0.0)) ).title("Rider's Location")) ) ;
        markers.add( mMap.addMarker(new MarkerOptions().position( latLng).title("Your Location")))  ;

        final LatLngBounds.Builder builder = new LatLngBounds.Builder() ;
            for( Marker mark : markers ){
                builder.include(mark.getPosition()) ;
            }
        LatLngBounds bounds = builder.build() ;

        int padding = 50 ;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds , padding) ;

        mMap.animateCamera(cu);

    }

    public void acceptRide(View view){

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Requests") ;
        query.whereEqualTo( "username" , username );
        Log.i("Searching" , username) ;
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size() > 0){
                        for( ParseObject object : objects){
                            Log.i("info", object.toString());
                            object.put("driverUsername" , ParseUser.getCurrentUser().getUsername());
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if( e==null ){
                                        Toast.makeText(driverMapView.this, "User Notified", Toast.LENGTH_SHORT).show();
                                        ParseUser.getCurrentUser().put("location" , new ParseGeoPoint(latLng.latitude , latLng.longitude));
                                        ParseUser.getCurrentUser().saveInBackground();
                                        Log.i("Updated User" , ParseUser.getCurrentUser().toString() );
                                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                Uri.parse("http://maps.google.com/maps?saddr="+markers.get(0).getPosition().latitude+","+markers.get(0).getPosition().longitude+"&daddr="+markers.get(1).getPosition().latitude+","+markers.get(1).getPosition().longitude));
                                        startActivity(intent);
                                    }
                                    else{
                                        e.printStackTrace();
                                        Toast.makeText(driverMapView.this, "Error Accepting The Ride", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    Toast.makeText(driverMapView.this , "No User Found" , Toast.LENGTH_SHORT).show();
                }
                else{
                    e.printStackTrace();
                    Toast.makeText(driverMapView.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void logout(View view){
        ParseUser.logOut();
        Intent i = new Intent(this , MainActivity.class);
        startActivity( i );
    }
}