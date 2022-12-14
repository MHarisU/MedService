package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.med.medservice.R;

public class BookAppointmentNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_appointment_new);
    }

    public void GoBackToMain(View view) {
        finish();
    }

    public void openAllDoctors(View view) {
        startActivity(new Intent(BookAppointmentNewActivity.this, AllDoctorsNewActivity.class));
    }
}