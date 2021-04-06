package com.med.medservice.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;

import com.med.medservice.PatientMainActivity;

public class MyCountDownTimer extends CountDownTimer {


    Context context;

    public MyCountDownTimer(Context context, long startTime, long interval) {
        super(startTime, interval);
        this.context = context;
    }



    @Override
    public void onFinish() {
        //DO WHATEVER YOU WANT HERE
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //
                //context.startService(new Intent(context, AuthService.class));
              //  checkAuthQueue = false;
            }
        }, 2000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
    }
}