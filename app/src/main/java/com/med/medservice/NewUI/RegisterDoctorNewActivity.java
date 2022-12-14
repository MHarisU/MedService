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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.med.medservice.R;

public class RegisterDoctorNewActivity extends AppCompatActivity {

    LinearLayout accountInfoLayout, personalInfoLayout, professionalInfoLayout, termsLayout, finishLayout;

    Spinner doctorGenderSpinner, doctorStateSpinner, doctorCitySpinner, doctorSpecializationSpinner, doctorLicensedStateSpinner;

    // For Registration Steps

    CardView doctorAccountInfoCard, doctorPersonalInfoCard, doctorProfessionalInfoCard, doctorTermsCard, doctorFinishRegCard;
    TextView docAccountInfoText, doctorPersonalInfoText, doctorProfessionalInfoText, doctorTermsText, doctorFinishRegText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_doctor_new);

        // Cards code For Registration Steps

        doctorAccountInfoCard = findViewById(R.id.docAccountInfoCard);
        doctorPersonalInfoCard = findViewById(R.id.docPersonalInfoCard);
        doctorProfessionalInfoCard = findViewById(R.id.docProfessionalInfoCard);
        doctorTermsCard = findViewById(R.id.docTermsCard);
        doctorFinishRegCard = findViewById(R.id.docFinishRegCard);

        docAccountInfoText = findViewById(R.id.docAccountInfoText);
        doctorPersonalInfoText = findViewById(R.id.docPersonalInfoText);
        doctorProfessionalInfoText = findViewById(R.id.docProfessionalInfoText);
        doctorTermsText = findViewById(R.id.docTermsText);
        doctorFinishRegText = findViewById(R.id.docFinishRegText);

        //  code for registration layouts

        accountInfoLayout = findViewById(R.id.accountInfoLayout);
        personalInfoLayout = findViewById(R.id.personalInfoLayout);
        professionalInfoLayout = findViewById(R.id.professionalInfoLayout);
        termsLayout = findViewById(R.id.termsLayout);
        finishLayout = findViewById(R.id.finishLayout);



        // spinners code

        doctorGenderSpinner = findViewById(R.id.genderSpinner);
        doctorStateSpinner = findViewById(R.id.stateSpinner);
        doctorCitySpinner = findViewById(R.id.citySpinner);
        doctorSpecializationSpinner = findViewById(R.id.doctorSpecialization);
        doctorLicensedStateSpinner = findViewById(R.id.doctorLicensedState);


        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorGenderSpinner.setAdapter(genderAdapter);

        doctorGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorStateSpinner.setAdapter(stateAdapter);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.Cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorCitySpinner.setAdapter(cityAdapter);


        ArrayAdapter<CharSequence> specializationSpinner = ArrayAdapter.createFromResource(this, R.array.doctorSpecialization, android.R.layout.simple_spinner_item);
        specializationSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpecializationSpinner.setAdapter(specializationSpinner);

        doctorSpecializationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> licensedStateSpinner = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        licensedStateSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorLicensedStateSpinner.setAdapter(licensedStateSpinner);



        setSpinner();

    }

    private void setSpinner() {
        Spinner spinner1 = findViewById(R.id.doctorSpecialization);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.doctorSpecialization, android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);



       /* ArrayAdapter<CharSequence> specializationSpinner = ArrayAdapter.createFromResource(this, R.array.doctorSpecialization, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpecializationSpinner.setAdapter(specializationSpinner);

        doctorSpecializationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }


    // Code for clicking buttons

    public void close(View view) {
        finish();
        startActivity(new Intent(RegisterDoctorNewActivity.this, LoginNewActivity.class));
    }


    public void openPersonalInfoLayout(View view) {
        accountInfoLayout.setVisibility(View.GONE);
        personalInfoLayout.setVisibility(View.VISIBLE);
        doctorPersonalInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorAccountInfoCard.setCardBackgroundColor(Color.GRAY);
        docAccountInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));
        doctorPersonalInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
    }

    public void backToAccountLayout(View view) {
        personalInfoLayout.setVisibility(View.GONE);
        accountInfoLayout.setVisibility(View.VISIBLE);
        doctorAccountInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorPersonalInfoCard.setCardBackgroundColor(Color.GRAY);
        docAccountInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorPersonalInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));
    }

    public void openProfessionalLayout(View view) {
        personalInfoLayout.setVisibility(View.GONE);
        professionalInfoLayout.setVisibility(View.VISIBLE);
        doctorPersonalInfoCard.setCardBackgroundColor(Color.GRAY);
        doctorProfessionalInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorProfessionalInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorPersonalInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));

    }

    public void backToPersonalInfoLayout(View view) {
        professionalInfoLayout.setVisibility(View.GONE);
        personalInfoLayout.setVisibility(View.VISIBLE);
        doctorPersonalInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorProfessionalInfoCard.setCardBackgroundColor(Color.GRAY);
        doctorProfessionalInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));
        doctorPersonalInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));


    }

    public void openTermsLayout(View view) {
        professionalInfoLayout.setVisibility(View.GONE);
        termsLayout.setVisibility(View.VISIBLE);
        doctorTermsCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorProfessionalInfoCard.setCardBackgroundColor(Color.GRAY);
        doctorTermsText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorProfessionalInfoText.setTextColor(this.getResources().getColor(R.color.GreyNew));
    }

    public void backToProfessionalLayout(View view) {
        termsLayout.setVisibility(View.GONE);
        professionalInfoLayout.setVisibility(View.VISIBLE);
        doctorTermsCard.setCardBackgroundColor(Color.GRAY);
        doctorProfessionalInfoCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorProfessionalInfoText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorTermsText.setTextColor(this.getResources().getColor(R.color.GreyNew));

    }

    public void openFinishLayout(View view) {
        termsLayout.setVisibility(View.GONE);
        finishLayout.setVisibility(View.VISIBLE);
        doctorTermsCard.setCardBackgroundColor(Color.GRAY);
        doctorFinishRegCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        doctorTermsText.setTextColor(this.getResources().getColor(R.color.GreyNew));
        doctorFinishRegText.setTextColor(this.getResources().getColor(R.color.DarkBlueNew));
    }


    public void openLogin(View view) {
        finish();
        startActivity(new Intent(RegisterDoctorNewActivity.this, LoginNewActivity.class));


    }
}