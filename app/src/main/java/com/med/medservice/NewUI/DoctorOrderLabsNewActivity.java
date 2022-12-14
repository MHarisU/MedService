package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class DoctorOrderLabsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_order_labs_new);
    }

    public void GoBackToMain(View view) {
        finish();
    }

    public void openGeneralHealthLabs(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, GeneralHealthLabTestActivity.class));

    }

    public void openWomenLabTests(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, WomenHealthLabTestsActivity.class));

    }

    public void openMenLabTests(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, MenHealthLAbTestsActivity.class));

    }

    public void openDigestiveLabTests(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, DigestiveHealthLabTestsActivity.class));

    }

    public void openHeartLabTests(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, HeartHealthLabTestsActivity.class));

    }

    public void openInfectiousLabTests(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, InfectiousDiseaseLabTestsActivity.class));

    }

    public void openSTDLabTests(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, STDLabTestsActivity.class));

    }

    public void openOtherLabTests(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, OtherLabTestsActivity.class));

    }

    public void openLabDetails(View view) {
        startActivity(new Intent(DoctorOrderLabsNewActivity.this, LabTestDetailsNewActivity.class));

    }
}