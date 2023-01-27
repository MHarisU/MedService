package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.R;

public class DoctorProfileNewActivity extends AppCompatActivity {

    CardView DocProfileCard, DocMedicalCard, DocActivityCard;
    TextView docProfileText, docMedicalText, docActivityText;

    LinearLayout docProfileLayout, docMedicalLayout, docActivityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_profile_new);

        setUpLayouts();

    }

    private void setUpLayouts() {

        DocProfileCard = findViewById(R.id.docprofileCard);
        DocMedicalCard = findViewById(R.id.docmedicalCard);
        DocActivityCard = findViewById(R.id.docactivityCard);

        docProfileText = findViewById(R.id.docProfileText);
        docMedicalText = findViewById(R.id.docMedicalText);
        docActivityText = findViewById(R.id.docActivityText);

        docProfileLayout = findViewById(R.id.docProfileLayout);
        docMedicalLayout = findViewById(R.id.docMedicalLayout);
        docActivityLayout = findViewById(R.id.docActivityLayout);

    }


    public void GoBackToMain(View view) {
        finish();
        startActivity(new Intent(DoctorProfileNewActivity.this, DoctorMainNewActivity.class));

    }


    public void openDocProfileDetails(View view) {
        docProfileLayout.setVisibility(View.VISIBLE);
        docMedicalLayout.setVisibility(View.GONE);
        docActivityLayout.setVisibility(View.GONE);
        DocProfileCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        DocMedicalCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        DocActivityCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        docProfileText.setTextColor(Color.WHITE);
        docMedicalText.setTextColor(Color.BLACK);
        docActivityText.setTextColor(Color.BLACK);

    }

    public void openDocCertificatesInfo(View view) {
        docProfileLayout.setVisibility(View.GONE);
        docMedicalLayout.setVisibility(View.VISIBLE);
        docActivityLayout.setVisibility(View.GONE);
        DocProfileCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        DocMedicalCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        DocActivityCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        docProfileText.setTextColor(Color.BLACK);
        docMedicalText.setTextColor(Color.WHITE);
        docActivityText.setTextColor(Color.BLACK);
    }

    public void openDocActivityInfo(View view) {
        docProfileLayout.setVisibility(View.GONE);
        docMedicalLayout.setVisibility(View.GONE);
        docActivityLayout.setVisibility(View.VISIBLE);
        DocProfileCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        DocMedicalCard.setCardBackgroundColor(this.getResources().getColor(R.color.white));
        DocActivityCard.setCardBackgroundColor(this.getResources().getColor(R.color.DarkBlueNew));
        docProfileText.setTextColor(Color.BLACK);
        docMedicalText.setTextColor(Color.BLACK);
        docActivityText.setTextColor(Color.WHITE);
    }

}