package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.R;

public class PatientVideoNewActivity extends AppCompatActivity {

    LinearLayout patientMedicationLayout, patientReportsLayout, patientHistoryLayout, reportsButtonsLayout, patientLabReports, patientImagingReports,
    patientMedicalHistory, patientVisitHistory, historyButtonsLayout;

    TextView medicationInfoButton, reportsInfoButton, historyInfoButton;

    boolean medicationInfoCheck = false;
    boolean reportsInfoCheck = false;
    boolean historyInfoCheck = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_video_new);

        setupLayouts();

    }

    private void setupLayouts() {


        patientMedicationLayout = findViewById(R.id.medicationLayout);
        patientReportsLayout = findViewById(R.id.reportsLayout);
        patientHistoryLayout = findViewById(R.id.historyLayout);

        medicationInfoButton = findViewById(R.id.medicationButton);
        reportsInfoButton = findViewById(R.id.reportsButton);
        historyInfoButton = findViewById(R.id.historyButton);

        reportsButtonsLayout = findViewById(R.id.reportsButtons);
        patientLabReports = findViewById(R.id.labReports);
        patientImagingReports = findViewById(R.id.imagingReports);

        patientMedicalHistory = findViewById(R.id.medicalHistory);
        patientVisitHistory = findViewById(R.id.visitHistory);
        historyButtonsLayout = findViewById(R.id.historyButtons);



    }


    public void openMedicationLayout(View view) {


        if (!medicationInfoCheck) {

            patientMedicationLayout.setVisibility(View.VISIBLE);
            patientReportsLayout.setVisibility(View.GONE);
            patientHistoryLayout.setVisibility(View.GONE);



            medicationInfoButton.setBackgroundResource(R.drawable.dark_blue_bg);
            medicationInfoButton.setTextColor(Color.WHITE);
            medicationInfoCheck = true;
        } else {
            patientMedicationLayout.setVisibility(View.GONE);
            patientReportsLayout.setVisibility(View.GONE);
            patientHistoryLayout.setVisibility(View.GONE);


            medicationInfoButton.setBackgroundColor(Color.WHITE);
            medicationInfoButton.setTextColor(Color.BLACK);
            medicationInfoCheck = false;
        }
        reportsInfoCheck = false;
        historyInfoCheck = false;

        reportsInfoButton.setBackgroundColor(Color.WHITE);
        reportsInfoButton.setTextColor(Color.BLACK);

        historyInfoButton.setBackgroundColor(Color.WHITE);
        historyInfoButton.setTextColor(Color.BLACK);



    }

    public void openReportsLayout(View view) {


        if (!reportsInfoCheck) {

            patientMedicationLayout.setVisibility(View.GONE);
            patientReportsLayout.setVisibility(View.VISIBLE);
            patientHistoryLayout.setVisibility(View.GONE);



            reportsInfoButton.setBackgroundResource(R.drawable.dark_blue_bg);
            reportsInfoButton.setTextColor(Color.WHITE);
            reportsInfoCheck = true;
        } else {
            patientMedicationLayout.setVisibility(View.GONE);
            patientReportsLayout.setVisibility(View.GONE);
            patientHistoryLayout.setVisibility(View.GONE);


            reportsInfoButton.setBackgroundColor(Color.WHITE);
            reportsInfoButton.setTextColor(Color.BLACK);
            reportsInfoCheck = false;
        }
        medicationInfoCheck = false;
        historyInfoCheck = false;

        medicationInfoButton.setBackgroundColor(Color.WHITE);
        medicationInfoButton.setTextColor(Color.BLACK);

        historyInfoButton.setBackgroundColor(Color.WHITE);
        historyInfoButton.setTextColor(Color.BLACK);



    }

    public void openHistoryLayout(View view) {


        if (!historyInfoCheck) {

            patientMedicationLayout.setVisibility(View.GONE);
            patientReportsLayout.setVisibility(View.GONE);
            patientHistoryLayout.setVisibility(View.VISIBLE);



            historyInfoButton.setBackgroundResource(R.drawable.dark_blue_bg);
            historyInfoButton.setTextColor(Color.WHITE);
            historyInfoCheck = true;
        } else {
            patientMedicationLayout.setVisibility(View.GONE);
            patientReportsLayout.setVisibility(View.GONE);
            patientHistoryLayout.setVisibility(View.GONE);


            historyInfoButton.setBackgroundColor(Color.WHITE);
            historyInfoButton.setTextColor(Color.BLACK);
            historyInfoCheck = false;
        }
        medicationInfoCheck = false;
        reportsInfoCheck = false;

        medicationInfoButton.setBackgroundColor(Color.WHITE);
        medicationInfoButton.setTextColor(Color.BLACK);

        reportsInfoButton.setBackgroundColor(Color.WHITE);
        reportsInfoButton.setTextColor(Color.BLACK);



    }

    public void close(View view) {
        finish();
    }

    public void openLabReports(View view) {
        patientLabReports.setVisibility(View.VISIBLE);
        reportsButtonsLayout.setVisibility(View.GONE);
        patientImagingReports.setVisibility(View.GONE);
    }

    public void openImagingReports(View view) {
        patientImagingReports.setVisibility(View.VISIBLE);
        patientLabReports.setVisibility(View.GONE);
        reportsButtonsLayout.setVisibility(View.GONE);
    }

    public void backToReportsLayout(View view) {
        reportsButtonsLayout.setVisibility(View.VISIBLE);
        patientLabReports.setVisibility(View.GONE);
        patientImagingReports.setVisibility(View.GONE);
    }

    public void openMedicalHistory(View view) {
        patientMedicalHistory.setVisibility(View.VISIBLE);
        historyButtonsLayout.setVisibility(View.GONE);
        patientVisitHistory.setVisibility(View.GONE);

    }

    public void openVisitHistory(View view) {
        patientVisitHistory.setVisibility(View.VISIBLE);
        patientMedicalHistory.setVisibility(View.GONE);
        historyButtonsLayout.setVisibility(View.GONE);

    }

    public void backToHistoryLayout(View view) {
        historyButtonsLayout.setVisibility(View.VISIBLE);
        patientVisitHistory.setVisibility(View.GONE);
        patientMedicalHistory.setVisibility(View.GONE);
    }
}
