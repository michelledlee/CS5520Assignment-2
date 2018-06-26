package edu.neu.madcourse.michellelee.numad18s_michellelee;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.widget.TextView;


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

    }
}
