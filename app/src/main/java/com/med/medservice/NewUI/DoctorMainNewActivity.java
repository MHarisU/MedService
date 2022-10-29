package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.PatientMainActivity;
import com.med.medservice.PharmacyActivity;
import com.med.medservice.R;

public class DoctorMainNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_main_new);




    }

    public void OpenWaitingRoomNew(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, WaitingRoomNewActivity.class));
    }

    public void openAllPatients(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, AllPatientsNewActivity.class));
    }

    public void openActivityDoctorSchedule(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, DoctorScheduleNewActivity.class));
    }


    public void OpenPendingAppointments(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, DoctorPendingAppointmentsNewActivity.class));
    }
}