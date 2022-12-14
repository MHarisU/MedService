package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class PatientProfileDetailsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_profile_details_new);
    }

    public void close(View view) {
        finish();
    }

    public void openSessionHistory(View view) {
        startActivity(new Intent(PatientProfileDetailsNewActivity.this, PatientSessionDetailsNewActivity.class));

    }

    public void openMedicalProfile(View view) {
        startActivity(new Intent(PatientProfileDetailsNewActivity.this, PatientMedicalProfileNewActivity.class));

    }

    public void openMedicationHistory(View view) {
        startActivity(new Intent(PatientProfileDetailsNewActivity.this, PatientMedicationHistoryNewActivity.class));

    }

    public void openLabHistory(View view) {
        startActivity(new Intent(PatientProfileDetailsNewActivity.this, PatientLabHistoryNewActivity.class));

    }

    public void openImagingHistory(View view) {
        startActivity(new Intent(PatientProfileDetailsNewActivity.this, PatientImagingHistoryNewActivity.class));

    }
}