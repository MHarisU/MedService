package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class DoctorSessionActivity extends AppCompatActivity {


    LinearLayout firstDec, secondOct;
    boolean firstDec_check = false;
    boolean secondOct_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_session);


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
        startActivity(new Intent(this, DoctorSessionDetailActivity.class));
    }
}