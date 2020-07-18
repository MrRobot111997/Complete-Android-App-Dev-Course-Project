package com.example.wearnotificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import androidx.core.app.NotificationBuilderWithBuilderAccessor;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE) ;

        NotificationChannel notificationChannel = new NotificationChannel("default" , "test" , NotificationManager.IMPORTANCE_DEFAULT) ;

        manager.createNotificationChannel(notificationChannel);


        Intent intent = new Intent(this , MainActivity.class) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , intent ,  0) ;
        Notification.Builder builder = new Notification.Builder(this , "default")
                .setContentTitle("Hello There")
                .setContentText("Hoew Are you Doing")
                .setSmallIcon(android.R.drawable.arrow_down_float)
                .setContentIntent(pendingIntent) ;

        manager.notify(0 , builder.build());

        // Enables Always-on
        setAmbientEnabled();
    }
}
