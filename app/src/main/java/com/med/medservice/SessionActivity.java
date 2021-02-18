package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SessionActivity extends AppCompatActivity {


    LinearLayout firstDec, secondOct;
    boolean firstDec_check = false;
    boolean secondOct_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_session);

        firstDec = findViewById(R.id.firstDec);
        secondOct = findViewById(R.id.secondOct);

    }

    public void Close(View view) {
        finish();
    }

    public void FirstDec(View view) {

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);



        if (!firstDec_check){
            firstDec.setVisibility(View.VISIBLE);
            firstDec.startAnimation(slide_down);
            firstDec_check = true;
        }
        else {
            firstDec.startAnimation(slide_up);

            firstDec.setVisibility(View.GONE);
            firstDec_check = false;
        }
    }

    public void SecondOct(View view) {


        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);



        if (!secondOct_check){
            secondOct.setVisibility(View.VISIBLE);
            secondOct.startAnimation(slide_down);
            secondOct_check = true;
        }
        else {
            secondOct.startAnimation(slide_up);

            secondOct.setVisibility(View.GONE);
            secondOct_check = false;
        }
    }

    public void OpenSessionDetail(View view) {
        startActivity(new Intent(getApplicationContext(), SessionDetailActivity.class));
    }
}