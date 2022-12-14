package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.med.medservice.R;

public class PatientDoctorsListNewActivity extends AppCompatActivity {

    Dialog docBioDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_doctors_list_new);

        setUpBioDialog();
    }

    private void setUpBioDialog() {
        docBioDialog = new Dialog(PatientDoctorsListNewActivity.this);
        docBioDialog.setContentView(R.layout.doctor_bio_dialog);
        docBioDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        docBioDialog.setCancelable(false);

        Button docBioButton = findViewById(R.id.viewDocBio);
        docBioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docBioDialog.show();

            }
        });


    }

    public void openBookingAppointmentActivity(View view) {
        startActivity(new Intent(PatientDoctorsListNewActivity.this, PatientBookingAppoitmentDetailsNewActivity.class));
    }

    public void GoBackToMain(View view) {
        finish();    }


    public void closeBio(View view) {
        docBioDialog.dismiss();
    }
}