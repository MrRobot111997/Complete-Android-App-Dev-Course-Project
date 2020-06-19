package com.example.favoritelocations;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ServiceConfigurationError;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker mark , userMark ;
    LocationManager manager ;
    LocationListener locationListener ;
    int intentInt ;

    public void centerMapOnLocation(final LatLng latlag){

        Log.i("Location" , latlag.toString()) ;
        if(latlag != null){

            if(intentInt == -1){
                if(mark!=null)
                    mark.remove();
                mark = mMap.addMarker(new MarkerOptions().position(latlag).title("You are Here"));
            }
            else if(intentInt == -2) {
                userMark = mMap.addMarker(new MarkerOptions().title("Saved Location").position(latlag).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                Toast.makeText(this , "Location Saved" , Toast.LENGTH_SHORT).show();
                intentInt = -1 ;
            }
            else
                userMark = mMap.addMarker(new MarkerOptions().title(MainActivity.addList.get(intentInt)).position(latlag).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))) ;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlag , 16));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
              if( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED  ){
                  manager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 60000, 0 , locationListener);
                  Location last = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;
                  if(last!=null) {
                      LatLng latLng = new LatLng(last.getLatitude(), last.getLongitude());
                      centerMapOnLocation(latLng);
                  }
              }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        Intent intent = getIntent() ;
        intentInt = intent.getIntExtra("index" , -1) ;
        if( intentInt == -1  ) {

            manager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centerMapOnLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
                Location last = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;
                LatLng latLng ;
                try {
                    latLng = new LatLng(last.getLatitude(), last.getLongitude());

                } catch(Exception e){
                     latLng = new LatLng(28.7041 ,77.1025 ) ;
                        Toast.makeText(this , "Kindly Switch ON your GPS." , Toast.LENGTH_LONG).show();
                }
                centerMapOnLocation(latLng);
            }
        }
        else{
            LatLng latLng = new LatLng(Double.parseDouble(MainActivity.lat.get(intent.getIntExtra("index" , -1))) ,
                                        Double.parseDouble(MainActivity.lon.get(intent.getIntExtra("index" , -1))) );
            centerMapOnLocation(latLng);
        }


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                intentInt = -2 ;

                centerMapOnLocation(latLng);

                Geocoder geocoder = new Geocoder(getApplicationContext()) ;

                try {
                    Address curr = geocoder.getFromLocation(latLng.latitude , latLng.longitude ,1).get(0);

                    Log.i("Address" , curr.toString() );

                    MainActivity.addList.add(curr.getAddressLine(0)) ;

                    MainActivity.lat.add(Double.toString(latLng.latitude)) ;

                    MainActivity.lon.add(Double.toString(latLng.longitude));

                    MainActivity.adapter.notifyDataSetChanged();

                    MainActivity.sharedPreferences.edit().putString("locations" ,  ObjectSerializer.serialize(MainActivity.addList)).apply();
                    MainActivity.sharedPreferences.edit().putString("lat" , ObjectSerializer.serialize(MainActivity.lat)).apply();
                    MainActivity.sharedPreferences.edit().putString("long" , ObjectSerializer.serialize(MainActivity.lon)).apply();

                } catch (Exception e) {
                    Log.i("No Address" , latLng.latitude+"  "+latLng.longitude) ;

                    e.printStackTrace();
                }
            }
        });
    }
}