package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class DoctorPendingAppointmentsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_pending_appointments_new);
    }

    public void GoBackToMain(View view) {
        finish();
    }

    public void openPendingAppointmentsDetails(View view) {
        startActivity(new Intent(DoctorPendingAppointmentsNewActivity.this, PendingAppointmentsDetailNewActivity.class));

    }

    public void openNotifications(View view) {
        startActivity(new Intent(DoctorPendingAppointmentsNewActivity.this, NotificationsNewActivity.class));

    }

    public void openCartActivity(View view) {
        startActivity(new Intent(DoctorPendingAppointmentsNewActivity.this, CartNewActivity.class));

    }
}