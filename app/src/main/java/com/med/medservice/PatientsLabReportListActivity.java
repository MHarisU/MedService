package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PatientsLabReportListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_lab_report_list);
    }

    public void Close(View view) {
        finish();
    }

    public void openLabReportView(View view) {
        startActivity(new Intent(this, LabReportViewActivity.class));
    }
}