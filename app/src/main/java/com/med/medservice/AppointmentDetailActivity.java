package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.med.medservice.Models.PatientAppointments.AppointmentList;
import com.med.medservice.Models.ProductMedicine.MedicineList;

public class AppointmentDetailActivity extends AppCompatActivity {

    AppointmentList currentData;

    TextView doctorName, appoitmentTime, appointmentDate, appointmentStatus, appointmentDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_appointment_detail);

        Intent intent = getIntent();

        currentData = (AppointmentList) intent.getSerializableExtra("selectedAppointment");

        doctorName = findViewById(R.id.doctorName);
        appoitmentTime = findViewById(R.id.appoitmentTime);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentStatus = findViewById(R.id.appointmentStatus);
        appointmentDay = findViewById(R.id.appointmentDay);

        doctorName.setText(currentData.getDoctor_name());
        appoitmentTime.setText(currentData.getTime());
        appointmentDate.setText(currentData.getDate());
        appointmentStatus.setText(currentData.getStatus());
        appointmentDay.setText(currentData.getDay());

    }

    public void AppointmentCancel(View view) {
        finish();
    }

    public void Close(View view) {
        finish();
    }
}