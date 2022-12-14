package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class AllPatientsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_patients_new);
    }

    public void GoBackToMain(View view) {
        //startActivity(new Intent(AllPatientsNewActivity.this, DoctorMainNewActivity.class));
        finish();
    }

    public void openPatientProfileDetails(View view) {
        startActivity(new Intent(AllPatientsNewActivity.this, PatientProfileDetailsNewActivity.class));

    }

    public void openCartActivity(View view) {
        startActivity(new Intent(AllPatientsNewActivity.this, CartNewActivity.class));

    }

    public void openNotifications(View view) {
        startActivity(new Intent(AllPatientsNewActivity.this, NotificationsNewActivity.class));

    }
}