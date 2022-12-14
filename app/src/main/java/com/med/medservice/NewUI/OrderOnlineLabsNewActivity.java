package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class OrderOnlineLabsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_online_labs_new);
    }

    public void GoBackToMain(View view) {
        finish();
    }

    public void openPatientGeneralLabs(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, GeneralHealthLabTestActivity.class));

    }

    public void openPatientWomenLabs(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, WomenHealthLabTestsActivity.class));

    }

    public void openPatientMenLabs(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, MenHealthLAbTestsActivity.class));
    }

    public void openPatientDigestiveLabs(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, DigestiveHealthLabTestsActivity.class));
    }

    public void openPatientHeartLabs(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, HeartHealthLabTestsActivity.class));
    }

    public void openPatientSTDLabs(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, STDLabTestsActivity.class));
    }

    public void openPatientInfectiousLabs(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, InfectiousDiseaseLabTestsActivity.class));
    }

    public void openPatientOtherLabs(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, OtherLabTestsActivity.class));
    }

    public void openLabDetails(View view) {
        startActivity(new Intent(OrderOnlineLabsNewActivity.this, LabTestDetailsNewActivity.class));
    }
}