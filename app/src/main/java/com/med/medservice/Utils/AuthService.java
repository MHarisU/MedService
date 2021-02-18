package com.med.medservice.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.med.medservice.AuthActivity;
import com.med.medservice.LoginActivity;
import com.med.medservice.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AuthService extends Service {

    //String

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // RepeatTask();
        AgainAuthenticate();
        return START_NOT_STICKY;
    }

    private void AgainAuthenticate() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Intent i = new Intent(getBaseContext(), AuthActivity.class);
                i.putExtra("checkActivity", "fronService");
                //   i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(i);
                stopSelf();

            }
        }, 3000);

    }


    private void RepeatTask() {


        Timer t = new Timer();
        //Set the schedule function and rate
        final int[] i = {0};
        Log.d("TASK : ", "" + i[0]);

        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {

                                      i[0]++;
                                      Log.d("TASK : ", "" + i[0]);

                                      Intent i = new Intent(getBaseContext(), AuthActivity.class);
                                      i.putExtra("checkActivity", "fronService");
                                      //   i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                      getApplication().startActivity(i);

                                  }

                              },
                //Set how long before to start calling the TimerTask (in milliseconds)
                20000,
                //Set the amount of time between each execution (in milliseconds)
                20000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopSelf();
        this.stopSelf();
    }


}
