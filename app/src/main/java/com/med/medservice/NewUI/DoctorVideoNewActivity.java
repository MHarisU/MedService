package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.med.medservice.DoctorVideoActivity;
import com.med.medservice.R;
import com.med.medservice.Utils.OpenPrescribedItems;

public class DoctorVideoNewActivity extends AppCompatActivity {

    LinearLayout patientInfoLayout, prescriptionInfoLayout, referInfoLayout, notesInfoLayout, prescribeLayout, medicineCategoriesLayout, labTestCategoriesLayout, imagingCategoriesLayout;

    TextView patientInfoButton, prescriptionInfoButton, referInfoButton, notesInfoButton;

    Spinner specializationSpinner;

    Button prescribeButton;

    boolean patInfoCheck = false;
    boolean prescriptionInfoCheck = false;
    boolean referInfoCheck = false;
    boolean notesInfoCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_video_new);


        setupLayouts();

        setUpSpinner();
    }

    private void setUpSpinner() {
        specializationSpinner = findViewById(R.id.doctorSpecializationSpinner);
        ArrayAdapter<CharSequence> specializationAdapter = ArrayAdapter.createFromResource(this, R.array.doctorSpecialization, android.R.layout.simple_spinner_dropdown_item);
        specializationSpinner.setAdapter(specializationAdapter);


    }

    private void setupLayouts() {

        patientInfoLayout = findViewById(R.id.patientInfoLayout);
        prescriptionInfoLayout = findViewById(R.id.prescriptionInfoLayout);
        referInfoLayout = findViewById(R.id.referInfoLayout);
        notesInfoLayout = findViewById(R.id.notesInfoLayout);


        patientInfoButton = findViewById(R.id.patientInfoButton);
        prescriptionInfoButton = findViewById(R.id.prescriptionInfoButton);
        referInfoButton = findViewById(R.id.referInfoButton);
        notesInfoButton = findViewById(R.id.notesButton);

        prescribeLayout = findViewById(R.id.prescribeLayout);

        prescribeButton = findViewById(R.id.prescribeButton);

        medicineCategoriesLayout = findViewById(R.id.medicineCategories);
        labTestCategoriesLayout = findViewById(R.id.labTestCategories);
        imagingCategoriesLayout = findViewById(R.id.imagingCategories);


    }

    public void close(View view) {
        finish();
    }


    public void OpenPatientDetails(View view) {


        if (!patInfoCheck) {

            patientInfoLayout.setVisibility(View.VISIBLE);
            prescriptionInfoLayout.setVisibility(View.GONE);
            referInfoLayout.setVisibility(View.GONE);
            notesInfoLayout.setVisibility(View.GONE);


            patientInfoButton.setBackgroundResource(R.drawable.dark_blue_bg);
            patientInfoButton.setTextColor(Color.WHITE);
            patInfoCheck = true;
        } else {
            patientInfoLayout.setVisibility(View.GONE);
            prescriptionInfoLayout.setVisibility(View.GONE);
            referInfoLayout.setVisibility(View.GONE);
            notesInfoLayout.setVisibility(View.GONE);


            patientInfoButton.setBackgroundColor(Color.WHITE);
            patientInfoButton.setTextColor(Color.BLACK);
            patInfoCheck = false;
        }
        prescriptionInfoCheck = false;
        referInfoCheck = false;
        notesInfoCheck = false;

        prescriptionInfoButton.setBackgroundColor(Color.WHITE);
        prescriptionInfoButton.setTextColor(Color.BLACK);

        referInfoButton.setBackgroundColor(Color.WHITE);
        referInfoButton.setTextColor(Color.BLACK);

        notesInfoButton.setBackgroundColor(Color.WHITE);
        notesInfoButton.setTextColor(Color.BLACK);

    }


    public void openPrescriptionLayout(View view) {


        if (!prescriptionInfoCheck) {
            patientInfoLayout.setVisibility(View.GONE);
            prescriptionInfoLayout.setVisibility(View.VISIBLE);
            referInfoLayout.setVisibility(View.GONE);
            notesInfoLayout.setVisibility(View.GONE);


            prescriptionInfoButton.setBackgroundResource(R.drawable.dark_blue_bg);
            prescriptionInfoButton.setTextColor(Color.WHITE);
            prescriptionInfoCheck = true;
            patInfoCheck = false;
            referInfoCheck = false;
            notesInfoCheck = false;
        } else {
            patientInfoLayout.setVisibility(View.GONE);
            prescriptionInfoLayout.setVisibility(View.GONE);
            referInfoLayout.setVisibility(View.GONE);
            notesInfoLayout.setVisibility(View.GONE);


            prescriptionInfoButton.setBackgroundColor(Color.WHITE);
            prescriptionInfoButton.setTextColor(Color.BLACK);
            prescriptionInfoCheck = false;
            patInfoCheck = false;
            referInfoCheck = false;
            notesInfoCheck = false;
        }


        patientInfoButton.setBackgroundColor(Color.WHITE);
        patientInfoButton.setTextColor(Color.BLACK);

        referInfoButton.setBackgroundColor(Color.WHITE);
        referInfoButton.setTextColor(Color.BLACK);

        notesInfoButton.setBackgroundColor(Color.WHITE);
        notesInfoButton.setTextColor(Color.BLACK);


    }

    public void openReferLayout(View view) {

        if (!referInfoCheck) {
            patientInfoLayout.setVisibility(View.GONE);
            prescriptionInfoLayout.setVisibility(View.GONE);
            referInfoLayout.setVisibility(View.VISIBLE);
            notesInfoLayout.setVisibility(View.GONE);


            referInfoButton.setBackgroundResource(R.drawable.dark_blue_bg);
            referInfoButton.setTextColor(Color.WHITE);
            referInfoCheck = true;
            patInfoCheck = false;
            prescriptionInfoCheck = false;
            notesInfoCheck = false;
        } else {

            patientInfoLayout.setVisibility(View.GONE);
            prescriptionInfoLayout.setVisibility(View.GONE);
            referInfoLayout.setVisibility(View.GONE);
            notesInfoLayout.setVisibility(View.GONE);


            referInfoButton.setBackgroundColor(Color.WHITE);
            referInfoButton.setTextColor(Color.BLACK);
            referInfoCheck = false;

            patInfoCheck = false;
            prescriptionInfoCheck = false;
            notesInfoCheck = false;
        }

        patientInfoButton.setBackgroundColor(Color.WHITE);
        patientInfoButton.setTextColor(Color.BLACK);

        prescriptionInfoButton.setBackgroundColor(Color.WHITE);
        prescriptionInfoButton.setTextColor(Color.BLACK);

        notesInfoButton.setBackgroundColor(Color.WHITE);
        notesInfoButton.setTextColor(Color.BLACK);
    }

    public void openNotesLayout(View view) {

        if (!notesInfoCheck) {

            patientInfoLayout.setVisibility(View.GONE);
            prescriptionInfoLayout.setVisibility(View.GONE);
            referInfoLayout.setVisibility(View.GONE);
            notesInfoLayout.setVisibility(View.VISIBLE);

            notesInfoButton.setBackgroundResource(R.drawable.dark_blue_bg);
            notesInfoButton.setTextColor(Color.WHITE);
            notesInfoCheck = true;
            patInfoCheck = false;
            prescriptionInfoCheck = false;
            referInfoCheck = false;
        } else {

            patientInfoLayout.setVisibility(View.GONE);
            prescriptionInfoLayout.setVisibility(View.GONE);
            referInfoLayout.setVisibility(View.GONE);
            notesInfoLayout.setVisibility(View.GONE);

            notesInfoButton.setBackgroundColor(Color.WHITE);
            notesInfoButton.setTextColor(Color.BLACK);
            notesInfoCheck = false;
            patInfoCheck = false;
            prescriptionInfoCheck = false;
            referInfoCheck = false;
        }

        patientInfoButton.setBackgroundColor(Color.WHITE);
        patientInfoButton.setTextColor(Color.BLACK);

        prescriptionInfoButton.setBackgroundColor(Color.WHITE);
        prescriptionInfoButton.setTextColor(Color.BLACK);

        referInfoButton.setBackgroundColor(Color.WHITE);
        referInfoButton.setTextColor(Color.BLACK);

    }


    public void openPrescribeLayout(View view) {
        prescribeLayout.setVisibility(View.VISIBLE);
        prescribeButton.setVisibility(View.GONE);

        //Show and hide layout on button click
//        if (prescribeLayout.getVisibility() == View.VISIBLE) {
//            prescribeLayout.setVisibility(View.GONE);
//        } else {
//            prescribeLayout.setVisibility(View.VISIBLE);
//        }
    }

    public void backToPrescribeButton(View view) {
        prescribeLayout.setVisibility(View.GONE);
        prescribeButton.setVisibility(View.VISIBLE);
    }

    public void backToButtonsLayout(View view) {
        prescribeLayout.setVisibility(View.VISIBLE);
        medicineCategoriesLayout.setVisibility(View.GONE);
        labTestCategoriesLayout.setVisibility(View.GONE);
        imagingCategoriesLayout.setVisibility(View.GONE);


    }

    public void openMedicineCategories(View view) {
        medicineCategoriesLayout.setVisibility(View.VISIBLE);
        prescribeLayout.setVisibility(View.GONE);
        labTestCategoriesLayout.setVisibility(View.GONE);
        imagingCategoriesLayout.setVisibility(View.GONE);
    }



    public void openLabTestCategories(View view) {
        labTestCategoriesLayout.setVisibility(View.VISIBLE);
        medicineCategoriesLayout.setVisibility(View.GONE);
        prescribeLayout.setVisibility(View.GONE);
        imagingCategoriesLayout.setVisibility(View.GONE);

    }

    public void openImagingCategories(View view) {
        imagingCategoriesLayout.setVisibility(View.VISIBLE);
        medicineCategoriesLayout.setVisibility(View.GONE);
        prescribeLayout.setVisibility(View.GONE);
        labTestCategoriesLayout.setVisibility(View.GONE);
    }
}
