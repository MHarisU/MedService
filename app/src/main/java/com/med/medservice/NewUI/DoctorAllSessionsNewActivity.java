package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class DoctorAllSessionsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_all_sessions_new);
    }

    public void GoBackToMain(View view) {
        finish();
    }

    public void openSessionDetails(View view) {
        startActivity(new Intent(DoctorAllSessionsNewActivity.this, DoctorSessionDetailsNewActivity.class));

    }

    public void OpenCartActivity(View view) {
        startActivity(new Intent(DoctorAllSessionsNewActivity.this, CartNewActivity.class));

    }

    public void openNotifications(View view) {
        startActivity(new Intent(DoctorAllSessionsNewActivity.this, NotificationsNewActivity.class));

    }
}