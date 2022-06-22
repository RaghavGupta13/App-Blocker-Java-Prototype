package com.example.blockerjavaprototype;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the start blocking apps button by its id
        btn = findViewById(R.id.id_btn);

        //To disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Usage Access Settings
        enableUsageAccessSettings();

        //Start Blocking Apps
        startBlockingApps();
    }

    //Method to start the background service
    private void startBackgroundService(){
        Intent serviceIntent = new Intent(this, MyBackgroundService.class);
        startService(serviceIntent);
    }

    //Method to open usage access settings
    /*
    Usage access settings need to be enabled for the app
    so that our app can track the other apps on the phone
     */
    private void enableUsageAccessSettings(){
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);

    }

    //This method checks if the usage access is enabled or not and return a boolean value
    private boolean checkIfUsageAccess(){
        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    //This Method handles the click event for the 'start blocking app' button
    private void startBlockingApps(){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkIfUsageAccess()){
                    Toast.makeText(MainActivity.this, "Usage Access should be enabled", Toast.LENGTH_SHORT).show();
                    enableUsageAccessSettings();
                }
                else{
                    startBackgroundService();
                    finish();
                }

            }
        });

    }
}