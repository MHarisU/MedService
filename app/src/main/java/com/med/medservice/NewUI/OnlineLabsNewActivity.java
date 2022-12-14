package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class OnlineLabsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_online_labs_new);
    }

    public void GoBackToMain(View view) {
        finish();
    }

    public void openOrderLabsNew(View view) {
        startActivity(new Intent(OnlineLabsNewActivity.this, DoctorOrderLabsNewActivity.class));

    }

    public void openDoctorRequisitions(View view) {
        startActivity(new Intent(OnlineLabsNewActivity.this, DoctorRequisitionsNewActivity.class));

    }

    public void openPendingOnlineLabs(View view) {
        startActivity(new Intent(OnlineLabsNewActivity.this, DoctorPendingOnlineLabsActivity.class));

    }

    public void openApprovedLabs(View view) {
        startActivity(new Intent(OnlineLabsNewActivity.this, DoctorApprovedLabsNewActivity.class));
    }

    public void openPendingRequisitions(View view) {
        startActivity(new Intent(OnlineLabsNewActivity.this, DoctorPendingRequisitionsNewActivity.class));

    }

    public void openNotifications(View view) {
        startActivity(new Intent(OnlineLabsNewActivity.this, NotificationsNewActivity.class));

    }

    public void OpenCartActivity(View view) {
        startActivity(new Intent(OnlineLabsNewActivity.this, CartNewActivity.class));

    }
}