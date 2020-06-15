package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Telephony;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    LocationManager manager ;
    LocationListener locationListener ;
    TextView text ;
    TextView text1 ;

    public class DowloadWeather extends AsyncTask<String , Void , String>{

        URL url ;
        HttpURLConnection conn ;
        @Override
        protected String doInBackground(String... urls) {

            try {
                Log.i("URL" , urls[0]) ;

                url = new URL( urls[0] ) ;
                conn = (HttpURLConnection) url.openConnection() ;
                conn.connect();

                BufferedReader reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
                StringBuffer weather = new StringBuffer() ;
                String temp = "" ;
                while( (temp = reader.readLine()) != null){
                    weather.append(temp);
                }
                Log.i("weather" , weather.toString()) ;
                return weather.toString() ;

            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to get Weather " ;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try{
                JSONObject jsonObject = new JSONObject(s) ;

                String weather = jsonObject.getString("weather") ;

                JSONArray arr = new JSONArray(weather) ;

                for (int i=0;i<arr.length() ; i++){
                    JSONObject part = arr.getJSONObject(i) ;

                    weather = "Weather = " + part.getString("main") + "(" + part.getString("description") + ")" ;
                }
                text1.setText(weather);
            }catch (Exception e){
                e.printStackTrace();
                text1.setText(s);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
            startListening();
        }
    }

    public void startListening(){
        if( ContextCompat.checkSelfPermission( this , Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 0 , 0 , locationListener);
    }

    public void updateLocationInfo(Location location ){

        Log.i("location" , location.toString() ) ;
        Geocoder geocoder = new Geocoder(getApplicationContext() , Locale.getDefault()) ;

        String add = "Could not Find Address" ;
        try {
            List<Address> addressList =geocoder.getFromLocation(location.getLatitude() , location.getLongitude() ,1) ;
            if(addressList != null && addressList.size()>0) {
                Log.i("Address : \n", addressList.toString()) ;
                if (addressList.get(0).getAddressLine(0) != null) {
                    add = "" ;
                    add += add + "Address :" + addressList.get(0).getAddressLine(0) + "\n";
                }
                if(addressList.get(0).hasLatitude())
                    add += "\nLatitiude : " + addressList.get(0).getLatitude() + "\n";
                if(addressList.get(0).hasLongitude())
                    add += "\nLongitude : " + addressList.get(0).getLatitude() + "\n";

                add += "\nAccuracy = " +  location.getAccuracy() + "\n" ;
                add += "\nAltitude = " + location.getAltitude()  ;

                DowloadWeather weather = new DowloadWeather() ;
                try {
                    weather.execute("https://api.openweathermap.org/data/2.5/weather?q=" + addressList.get(0).getSubAdminArea().toLowerCase() + "&appid=c4b19b2fc65782a8b95b1818212fd86c").get() ;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        text.setText(add);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.textView2);
        text1 = findViewById(R.id.textView3) ;
        manager = (LocationManager) getSystemService(LOCATION_SERVICE) ;

        locationListener =  new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
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

        if(ContextCompat.checkSelfPermission( this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION  } , 1  );
        }
        else {
            manager.requestLocationUpdates( LocationManager.GPS_PROVIDER,3000,0 , locationListener);
            Location lastKnown = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ;
            if( lastKnown != null){
                updateLocationInfo(lastKnown);

            }
        }
    }
}