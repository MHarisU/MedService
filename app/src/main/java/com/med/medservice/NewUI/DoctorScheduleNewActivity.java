package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.med.medservice.R;

public class DoctorScheduleNewActivity extends AppCompatActivity {

    Button ScheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule_new);




        ScheduleButton = findViewById(R.id.ScheduleButton);
        ScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }




    public void openAddScheduleDoctorNew(View view) {
    }
}