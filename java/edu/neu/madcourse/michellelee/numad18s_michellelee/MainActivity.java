package edu.neu.madcourse.michellelee.numad18s_michellelee;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import edu.neu.madcourse.michellelee.numad18s_michellelee.firebaseMessaging.FCMActivity;
import edu.neu.madcourse.michellelee.numad18s_michellelee.firebaseMessaging.WordGameMessagingService;


public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SET UP TOOLBAR
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // SETTING UP VERSION NUMBER AND VERSION CODE DISPLAY
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);   // get package manager to access versions
            String verName = pInfo.versionName; // get version number
            int verCode = pInfo.versionCode;    // get version code
            // setting text for version number
            TextView versionTextCode = (TextView) findViewById(R.id.version_code);
            versionTextCode.setText("Version Code: " + verName);
            // setting text for version code
            TextView versionTextNumber = (TextView) findViewById(R.id.version_name);
            versionTextNumber.setText("Version Code: " + verCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // GETTING DEVICE NUMBER
        String deviceUniqueIdentifier = "";    // default setting if not set by getImei or getDeviceID

        // check if we do not have permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // ask for permissions if we do not
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        // set up telephony service
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // if able to access
        if (tm != null) {
            // use getIMEI if API >= 26
            if (Build.VERSION.SDK_INT >= 26) {
                try {
                    deviceUniqueIdentifier = tm.getImei();
                } catch (SecurityException e) {
                    deviceUniqueIdentifier = "access denied (IMEI)";
                }
            // use getDevice ID if < API level 26
            } else {
                try {
                    deviceUniqueIdentifier = tm.getDeviceId();
                } catch (SecurityException e) {
                    deviceUniqueIdentifier = "security exception (deviceID)";
                }
            }
        } else {
            deviceUniqueIdentifier = "not available";
        }

        // set text for IMEI
        TextView imeiText = (TextView) findViewById(R.id.imei);
        imeiText.setText("IMEI: " + deviceUniqueIdentifier);


        // SUBSCRIBE APP TO HIGH_SCORE SERVICE
        FirebaseMessaging.getInstance().subscribeToTopic("high_score");
        String msg = getString(R.string.msg_subscribed);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // CREATE NOTIFICATION CHANNEL
//        createNotificationChannel();
//
//        // BROADCAST MAANGER
//        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("edu.neu.madcourse.michellelee.numad18s_michellelee-FCMMESSAGE"));

    }

//    private BroadcastReceiver mHandler = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(MainActivity.this, "FINAL COUNTDOWN", Toast.LENGTH_LONG).show();
//            String title = intent.getStringExtra("title");
//            String message = intent.getStringExtra("message");
//
//            Intent mIntent = new Intent(MainActivity.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0 /* Request code */, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
//
//            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
//                    .setSmallIcon(R.drawable.foo)
//                    .setContentTitle(title)
//                    .setContentText(message)
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//
//        }
//    };

//    @Override
//    protected void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
//    }
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(description, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
}
