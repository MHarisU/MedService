package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.med.medservice.R;

public class RegisterPatientNewActivity extends AppCompatActivity {

    LinearLayout patientAccountInfo, patientPersonalInfoLayout, patientMedicalInfoLayout, patientTermsLayout, patientFinishLayout, patientRepresentativeLayout, patientLayout;
    RadioButton radioPatient, radioRepresentative;

    Spinner patientGenderSpinner, patientStateSpinner, patientCitySpinner;


    // For Registration Steps

    CardView patientAccountCard, PatientPersonalInfoCard, PatientMedicalInfoCard, PatientTermsCard, PatientFinishRegCard;
    TextView patientAccountText, PatientPersonalInfoText, PatientMedicalInfoText, PatientTermsText, PatientFinishRegText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_patient_new);


        // For Registration Steps card and text code

        patientAccountCard = findViewById(R.id.accountCard);
        PatientPersonalInfoCard = findViewById(R.id.personalCard);
        PatientMedicalInfoCard = findViewById(R.id.medicalInfoCard);
        PatientTermsCard = findViewById(R.id.termsCard);
        PatientFinishRegCard = findViewById(R.id.finishCard);


        patientAccountText = findViewById(R.id.accountInfoText);
        PatientPersonalInfoText = findViewById(R.id.personalInfoText);
        PatientMedicalInfoText = findViewById(R.id.medicalInfoText);
        PatientTermsText = findViewById(R.id.termsInfoText);
        PatientFinishRegText = findViewById(R.id.finishRegText);

        //Registration steps Layouts

        patientAccountInfo = findViewById(R.id.accountInfoLayout);
        patientPersonalInfoLayout = findViewById(R.id.personalInfoLayout);
        patientMedicalInfoLayout = findViewById(R.id.medicalInfoLayout);
        patientTermsLayout = findViewById(R.id.termsLayout);
        patientFinishLayout = findViewById(R.id.finishLayout);

        //Patient info layouts

            radioPatient = findViewById(R.id.radioPatientCheckbox);
        radioRepresentative = findViewById(R.id.radioRepresentativeCheckbox);

        patientRepresentativeLayout = findViewById(R.id.patientRepresentativeLayout);
        patientLayout = findViewById(R.id.patientLayout);

        //spinners
        patientGenderSpinner = findViewById(R.id.genderSpinner);
        patientStateSpinner = findViewById(R.id.stateSpinner);
        patientCitySpinner = findViewById(R.id.citySpinner);


        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientGenderSpinner.setAdapter(genderAdapter);



        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientStateSpinner.setAdapter(stateAdapter);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.Cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientCitySpinner.setAdapter(cityAdapter);


        // code for selecting checkbox
        setupRadioButton();
    }

    private void setupRadioButton() {


        radioRepresentative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioRepresentative.isChecked()) {
                    patientRepresentativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        radioPatient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radioPatient.isChecked()) {
                    patientRepresentativeLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    public void Close(View view) {
    }

    public void close(View view) {
        finish();
        startActivity(new Intent(RegisterPatientNewActivity.this, LoginNewActivity.class));
    }


    public void openPersonalInfoLayout(View view) {
        patientAccountInfo.setVisibility(View.GONE);
        patientPersonalInfoLayout.setVisibility(View.VISIBLE);
        PatientPersonalInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        patientAccountCard.setCardBackgroundColor(Color.GRAY);
        patientAccountText.setTextColor(this.getResources().getColor(R.color.GreyNew));
        PatientPersonalInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
    }


    public void backToAccountLayout(View view) {
        patientPersonalInfoLayout.setVisibility(View.GONE);
        patientAccountInfo.setVisibility(View.VISIBLE);
        patientAccountCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientPersonalInfoCard.setCardBackgroundColor(Color.GRAY);
        patientAccountText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientPersonalInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));

    }

    public void openMedicalLayout(View view) {
        patientPersonalInfoLayout.setVisibility(View.GONE);
        patientMedicalInfoLayout.setVisibility(View.VISIBLE);
        PatientPersonalInfoCard.setCardBackgroundColor(Color.GRAY);
        PatientMedicalInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientMedicalInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientPersonalInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));
    }

    public void backToPersonalInfoLayout(View view) {
        patientMedicalInfoLayout.setVisibility(View.GONE);
        patientPersonalInfoLayout.setVisibility(View.VISIBLE);
        PatientPersonalInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientMedicalInfoCard.setCardBackgroundColor(Color.GRAY);
        PatientMedicalInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));
        PatientPersonalInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
    }

    public void openTermsLayout(View view) {
        patientMedicalInfoLayout.setVisibility(View.GONE);
        patientTermsLayout.setVisibility(View.VISIBLE);
        PatientTermsCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientMedicalInfoCard.setCardBackgroundColor(Color.GRAY);
        PatientTermsText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientMedicalInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));

    }

    public void backToMedicalInfoLayout(View view) {
        patientTermsLayout.setVisibility(View.GONE);
        patientMedicalInfoLayout.setVisibility(View.VISIBLE);
        PatientTermsCard.setCardBackgroundColor(Color.GRAY);
        PatientMedicalInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientMedicalInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientTermsText.setTextColor(this.getResources().getColor(R.color.GreyNew));
    }

    public void openFinishLayout(View view) {
        patientTermsLayout.setVisibility(View.GONE);
        patientFinishLayout.setVisibility(View.VISIBLE);
        PatientTermsCard.setCardBackgroundColor(Color.GRAY);
        PatientFinishRegCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        PatientTermsText.setTextColor(this.getResources().getColor(R.color.GreyNew));
        PatientFinishRegText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
    }




    public void openLogin(View view) {
        finish();
        startActivity(new Intent(RegisterPatientNewActivity.this, LoginNewActivity.class));



    }
}