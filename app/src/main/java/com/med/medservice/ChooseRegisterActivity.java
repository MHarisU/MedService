package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ChooseRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_register);
    }

    public void OpenLogin(View view) {
        finish();
       // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    public void RegisterAsPatient(View view) {
        //finish();
         startActivity(new Intent(getApplicationContext(), PatientRegisterActivity.class));

    }

    public void RegisterAsDoctor(View view) {

        //finish();
        startActivity(new Intent(getApplicationContext(), DoctorRegisterActivity.class));
    }
}