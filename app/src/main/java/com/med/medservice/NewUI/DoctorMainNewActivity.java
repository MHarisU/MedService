package com.med.medservice.NewUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.med.medservice.DoctorMainActivity;
import com.med.medservice.DoctorProfileActivity;
import com.med.medservice.PatientMainActivity;
import com.med.medservice.PharmacyActivity;
import com.med.medservice.R;

public class DoctorMainNewActivity extends AppCompatActivity {

    NavigationView doctorNavigationView;
    DrawerLayout doctorDrawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_main_new);


        setUpNavigation();


    }

    private void setUpNavigation() {

        doctorNavigationView = findViewById(R.id.nav_view);
        doctorNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        Intent i = new Intent(DoctorMainNewActivity.this, DoctorProfileNewActivity.class);
                        // i.putExtra("studentInfo", studentList);
                        startActivity(i);
                        break;

                    case R.id.nav_web:

                        Uri uriUrl = Uri.parse("http://medical.suunnoo.com/");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                        break;


                    case R.id.nav_logout:
                        //sessionManager.logout();
                        break;


                    case R.id.nav_exit:

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorMainNewActivity.this, R.style.DialogTheme)
                                .setTitle("")
                                .setMessage("Do you want to close the app?")
                                .setCancelable(false)
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setNeutralButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //  Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                        //  homeIntent.addCategory(Intent.CATEGORY_HOME);
                                        //  homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //  startActivity(homeIntent);
                                        finish();
                                        System.exit(0);
                                    }
                                });
                        dialog.show();


                        break;
                }

                return false;
            }
        });

    }

    public void OpenDrawer(View view) {
        doctorDrawer = findViewById(R.id.drawer_layout);
        if (!doctorDrawer.isDrawerOpen(GravityCompat.START)) {
            doctorDrawer.openDrawer(GravityCompat.START);
        } else {
            doctorDrawer.closeDrawer(GravityCompat.END);
        }
    }



    public void OpenWaitingRoomNew(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, WaitingRoomNewActivity.class));
    }

    public void openAllPatientsNew(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, AllPatientsNewActivity.class));

    }

    public void OpenPendingAppointments(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, DoctorPendingAppointmentsNewActivity.class));
    }


    public void OpenTotalSessions(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, DoctorAllSessionsNewActivity.class));
    }


    public void OpenTotalEarnings(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, DoctorTotalEarningsNewActivity.class));
    }

    public void OpenOnlineLabs(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, OnlineLabsNewActivity.class));
    }

    public void OpenCartActivity(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, CartNewActivity.class));
    }

    public void OpenRefillRequest(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, RefillRequestNewActivity.class));
    }

    public void openAddSchedule(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, DoctorScheduleNewActivity.class));

    }

    public void openPatientReportsNew(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, PatientReportsNewActivity.class));
    }

    public void openNotifications(View view) {
        startActivity(new Intent(DoctorMainNewActivity.this, NotificationsNewActivity.class));

    }



}