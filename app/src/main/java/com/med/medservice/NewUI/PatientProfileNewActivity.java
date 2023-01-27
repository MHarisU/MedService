package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.R;

public class PatientProfileNewActivity extends AppCompatActivity {

    CardView profileCard, medicalCard, activityCard;
    TextView profileText, medicalText, activityText;

    LinearLayout profileLayout, medicalLayout, activityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_profile_new);


        setUpLayouts();


    }

    private void setUpLayouts() {

        profileCard = findViewById(R.id.profileCard);
        medicalCard = findViewById(R.id.medicalCard);
        activityCard = findViewById(R.id.activityCard);

        profileText = findViewById(R.id.profileText);
        medicalText = findViewById(R.id.medicalText);
        activityText = findViewById(R.id.activityText);

        profileLayout = findViewById(R.id.profileLayout);
        medicalLayout = findViewById(R.id.medicalLayout);
        activityLayout = findViewById(R.id.activityLayout);

    }

    public void GoBackToMain(View view) {
        finish();
        startActivity(new Intent(PatientProfileNewActivity.this, PatientMainNewActivity.class));
    }

    public void openProfileDetails(View view) {
        profileLayout.setVisibility(View.VISIBLE);
        medicalLayout.setVisibility(View.GONE);
        activityLayout.setVisibility(View.GONE);
        profileCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        medicalCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        activityCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        profileText.setTextColor(Color.WHITE);
        medicalText.setTextColor(Color.BLACK);
        activityText.setTextColor(Color.BLACK);

    }

    public void openMedicalInfo(View view) {
        profileLayout.setVisibility(View.GONE);
        medicalLayout.setVisibility(View.VISIBLE);
        activityLayout.setVisibility(View.GONE);
        profileCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        medicalCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        activityCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        profileText.setTextColor(Color.BLACK);
        medicalText.setTextColor(Color.WHITE);
        activityText.setTextColor(Color.BLACK);
    }

    public void openActivityInfo(View view) {
        profileLayout.setVisibility(View.GONE);
        medicalLayout.setVisibility(View.GONE);
        activityLayout.setVisibility(View.VISIBLE);
        profileCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        medicalCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        activityCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        profileText.setTextColor(Color.BLACK);
        medicalText.setTextColor(Color.BLACK);
        activityText.setTextColor(Color.WHITE);
    }
}