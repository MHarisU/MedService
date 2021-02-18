package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DoctorProfileActivity extends AppCompatActivity {


    LinearLayout personal_info, earning_info, patient_timeline;

    ImageView timelineButton, earningButton, profileButton;

    LinearLayout august_month, july_month;
    boolean august_month_check = false;
    boolean july_month_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_profile);


        personal_info = findViewById(R.id.personal_info);
        earning_info = findViewById(R.id.earning_info);
        patient_timeline = findViewById(R.id.patient_timeline);

        profileButton = findViewById(R.id.profileButton);
        earningButton = findViewById(R.id.earningButton);
        timelineButton = findViewById(R.id.timelineButton);

        august_month = findViewById(R.id.august_month);
        july_month = findViewById(R.id.july_month);
    }


    public void Close(View view) {
        finish();
    }

    public void ProfileClick(View view) {
        personal_info.setVisibility(View.VISIBLE);
        earning_info.setVisibility(View.GONE);
        patient_timeline.setVisibility(View.GONE);

        profileButton.setBackgroundColor(Color.parseColor("#2c98f0"));
        earningButton.setBackgroundColor(Color.parseColor("#CC111111"));
        timelineButton.setBackgroundColor(Color.parseColor("#CC111111"));


    }

    public void EarningReportClick(View view) {
        personal_info.setVisibility(View.GONE);
        earning_info.setVisibility(View.VISIBLE);
        patient_timeline.setVisibility(View.GONE);

        profileButton.setBackgroundColor(Color.parseColor("#CC111111"));
        earningButton.setBackgroundColor(Color.parseColor("#2c98f0"));
        timelineButton.setBackgroundColor(Color.parseColor("#CC111111"));
    }

    public void TimelineClick(View view) {
        personal_info.setVisibility(View.GONE);
        earning_info.setVisibility(View.GONE);
        patient_timeline.setVisibility(View.VISIBLE);

        profileButton.setBackgroundColor(Color.parseColor("#CC111111"));
        earningButton.setBackgroundColor(Color.parseColor("#CC111111"));
        timelineButton.setBackgroundColor(Color.parseColor("#2c98f0"));
    }

    public void ClickAugust(View view) {

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);



        if (!august_month_check){
            august_month.setVisibility(View.VISIBLE);
            august_month.startAnimation(slide_down);
            august_month_check = true;
        }
        else {
            august_month.startAnimation(slide_up);

            august_month.setVisibility(View.GONE);
            august_month_check = false;
        }
    }

    public void ClickJuly(View view) {

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);



        if (!july_month_check){
            july_month.setVisibility(View.VISIBLE);
            july_month.startAnimation(slide_down);
            july_month_check = true;
        }
        else {
            july_month.startAnimation(slide_up);

            july_month.setVisibility(View.GONE);
            july_month_check = false;
        }
    }
}