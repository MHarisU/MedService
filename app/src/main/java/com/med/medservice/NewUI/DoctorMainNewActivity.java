package com.med.medservice.NewUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.med.medservice.DoctorMainActivity;
import com.med.medservice.DoctorProfileActivity;
import com.med.medservice.PatientMainActivity;
import com.med.medservice.PharmacyActivity;
import com.med.medservice.R;
import com.med.medservice.Utils.SessionManager;

public class DoctorMainNewActivity extends AppCompatActivity {

    NavigationView doctorNavigationView;
    DrawerLayout doctorDrawer;

    SessionManager sessionManager;

    Dialog exitAppDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_main_new);


        setUpNavigation();

        sessionManager = new SessionManager(this);


    }

    private void setUpNavigation() {

        doctorNavigationView = findViewById(R.id.nav_view);
        doctorNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navProfile:
                        Intent i = new Intent(DoctorMainNewActivity.this, DoctorProfileNewActivity.class);
                        startActivity(i);
                        break;

                    case R.id.navWeb:

                        Uri uriUrl = Uri.parse("https://www.umbrellamd.com/");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                        break;


                    case R.id.navLogout:
//                        sessionManager.logout();
                        break;


                    case R.id.navExit:

                        exitAppDialog = new Dialog(DoctorMainNewActivity.this);
                        exitAppDialog.setContentView(R.layout.close_app_dialog);
                        exitAppDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        exitAppDialog.setCancelable(false);
                        exitAppDialog.show();


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


    public void closeDialog(View view) {
        exitAppDialog.dismiss();
    }

    public void exitApp(View view) {
        finish();
        System.exit(0);
    }



}