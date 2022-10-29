package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class LoginNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_new);
    }

    public void LoginButtonClick(View view) {
        startActivity(new Intent(LoginNewActivity.this, DoctorMainNewActivity.class));
    }

    public void OpenRegisterPatientNew(View view) {
        startActivity(new Intent(LoginNewActivity.this, RegisterPatientNewActivity.class));
    }
}