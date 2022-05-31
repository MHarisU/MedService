package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class WalletActivity extends AppCompatActivity {


    LinearLayout august_month, july_month;
    boolean august_month_check = false;
    boolean july_month_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);




        august_month = findViewById(R.id.august_month);
        july_month = findViewById(R.id.july_month);
    }

    public void Close(View view) {
        finish();
    }

    public void ClickJuly(View view) {

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        if (!july_month_check) {
            july_month.setVisibility(View.VISIBLE);
            july_month.startAnimation(slide_down);
            july_month_check = true;
        } else {
            july_month.startAnimation(slide_up);

            july_month.setVisibility(View.GONE);
            july_month_check = false;
        }
    }

    public void ClickAugust(View view) {

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        if (!august_month_check) {
            august_month.setVisibility(View.VISIBLE);
            august_month.startAnimation(slide_down);
            august_month_check = true;
        } else {
            august_month.startAnimation(slide_up);

            august_month.setVisibility(View.GONE);
            august_month_check = false;
        }
    }

}