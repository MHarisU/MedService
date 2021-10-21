package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.med.medservice.Utils.SessionManager;

public class DoctorAppointmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_appointments);
    }

    public void Close(View view) {
        finish();
    }

    public void OpenHome(View view) {
        SessionManager sessionManager = new SessionManager(this);

        Intent i =null;

        if (sessionManager.getUserType().equals("patient")) {
            i = new Intent(this, PatientMainActivity.class);
        }
        else if (sessionManager.getUserType().equals("doctor")) {
            i = new Intent(this, DoctorMainActivity.class);
        }
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }
}