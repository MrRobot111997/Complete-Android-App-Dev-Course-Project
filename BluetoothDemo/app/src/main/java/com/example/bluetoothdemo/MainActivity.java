package com.example.bluetoothdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView statusText ;
    Button button ;
    ListView listView ;
    ArrayAdapter<String> arrayAdapter ;
    BluetoothAdapter bluetoothAdapter ;
    ArrayList<String> btInfo= new ArrayList<>() ;
    ArrayList<String> btAdd = new ArrayList<>() ;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction() ;
            Log.i("Action" , action ) ;

            if( BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) ){
                statusText.setText("Finished");
                button.setEnabled(true);
            }
            else if( BluetoothDevice.ACTION_FOUND.equals(action) ){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ;
                String add = device.getAddress() ;
                String rssi = Integer.toString(intent.getIntExtra(BluetoothDevice.EXTRA_RSSI , Short.MIN_VALUE ));

                if( !btAdd.contains(add) ){
                    btAdd.add(add) ;
                    btInfo.add(device.getName() +" "+add+" "+rssi  ) ;
                    arrayAdapter.notifyDataSetChanged();
                }
//                 Log.i( "Device INfo" , "Name = "+name+ " Address = "+ add +" RSSI = "+rssi) ;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( MainActivity.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 0);
        }


        button = findViewById(R.id.button);
        statusText = findViewById(R.id.textView) ;
        listView = findViewById(R.id.list);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() ;

        if( bluetoothAdapter==null ){
            Toast.makeText(this , "Your Device Doesn't Support Bluetooth" , Toast.LENGTH_SHORT).show();
        }

//        if( !bluetoothAdapter.isEnabled() ){
//            Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE) ;
//            startActivityForResult(enableBt , 1);
//        }

        IntentFilter intentFilter = new IntentFilter() ;
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver( broadcastReceiver , intentFilter) ;

        arrayAdapter = new ArrayAdapter<String>( getApplicationContext() , android.R.layout.simple_list_item_1 , btInfo ) ;
        listView.setAdapter( arrayAdapter );
    }

    public void onSearch( View view ){
            statusText.setText("Searching...");
            button.setEnabled(false);
            bluetoothAdapter.startDiscovery() ;
            btInfo.clear();
            btAdd.clear() ;
            arrayAdapter.notifyDataSetChanged();
    }
}