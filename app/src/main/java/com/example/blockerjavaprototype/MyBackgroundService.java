package com.example.blockerjavaprototype;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyBackgroundService extends Service {

    /*
    Background service will keep on running until the user terminates the app.
    In most case scenarios, user will only press back button to close the app.
    The background service will keep checking usage stats for the system every 2 secs and
    as soon as the top package name (the last opened app by the user) matches the hardcoded package
    name 'com.google.android.gm' (package for the GMail app), it will send an intent to start a new
    activity which is our Lock Activity.
    Therefore, every time a user opens the Gmail app, the lock activity screen will get displayed blocking the user's
    access to GMail.
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(
                new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
                    @Override
                    public void run() {
                        while(true) {
                            Log.d(TAG, "run: " + "Service is running");
                            String topPackageName;
                            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                            long time = System.currentTimeMillis();
                            // We get usage stats for the last 10 seconds
                            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
                            // Sort the stats by the last time used
                            if (stats != null) {
                                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                                for (UsageStats usageStats : stats) {
                                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                                }

                                if (!mySortedMap.isEmpty()) {
                                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                                    Log.d("TopPackage Name", topPackageName);

                                    if (topPackageName.equals("com.google.android.gm")) {
                                        //Log.d(TAG, "Current Package: " + topPackageName);
                                        Intent startAct = new Intent(getApplicationContext(), LockActivity.class);
                                        startAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(startAct);

                                    }
                                }


                            }

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
