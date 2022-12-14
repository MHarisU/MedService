package com.med.medservice.NewUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;
import com.med.medservice.OrderHistoryActivity;
import com.med.medservice.PatientMainActivity;
import com.med.medservice.PatientProfileActivity;
import com.med.medservice.R;
import com.med.medservice.Utils.AuthService;

public class PatientMainNewActivity extends AppCompatActivity {


    NavigationView patientNavigationView;
    DrawerLayout patientDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_main_new);



        setUpNavigation();
        
    }

    private void setUpNavigation() {

        patientDrawer = findViewById(R.id.patientDrawerLayout);
        patientNavigationView = findViewById(R.id.nav_view);
        patientNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_profile:
                        startActivity(new Intent(PatientMainNewActivity.this, PatientProfileNewActivity.class));
                        return true;

                }
                return false;
            }
        });

    }


    public void OpenDrawer(View view) {
        patientDrawer = findViewById(R.id.patientDrawerLayout);
        if (!patientDrawer.isDrawerOpen(GravityCompat.START)) {
            patientDrawer.openDrawer(GravityCompat.START);
        } else {
            patientDrawer.closeDrawer(GravityCompat.END);
        }
    }


    public void openPatientEVisit(View view) {
            startActivity(new Intent(PatientMainNewActivity.this, PatientE_VisitNewActivity.class));

    }

    public void oPenBookAppointment(View view) {
        startActivity(new Intent(PatientMainNewActivity.this, BookAppointmentNewActivity.class));
    }

    public void openOrderOnlineLabs(View view) {
        startActivity(new Intent(PatientMainNewActivity.this, OrderOnlineLabsNewActivity.class));
    }

    public void openPharmacy(View view) {
        startActivity(new Intent(PatientMainNewActivity.this, PharmacyNewMainActivity.class));
    }

    public void openMyDoctorsActivity(View view) {
        startActivity(new Intent(PatientMainNewActivity.this, PatientDoctorsListNewActivity.class));
    }

    public void openPatientOrders(View view) {
        startActivity(new Intent(PatientMainNewActivity.this, PatientOrdersNewActivity.class));

    }

    public void openPatientAppointments(View view) {
        startActivity(new Intent(PatientMainNewActivity.this, PatientAppointmentsNewActivity.class));
    }

    public void OpenCartActivity(View view) {
        startActivity(new Intent(PatientMainNewActivity.this, CartNewActivity.class));

    }

    public void openNotifications(View view) {
        startActivity(new Intent(PatientMainNewActivity.this, NotificationsNewActivity.class));

    }
}

