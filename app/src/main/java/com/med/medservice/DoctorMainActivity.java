package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.med.medservice.NetworkAPI.ApiCallerNew;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DoctorMainActivity extends AppCompatActivity {

    String name, user_id, email;
    SessionManager sessionManager;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_main);

        setUpNavigation();


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();


        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);
        TextView Name;
        Name = findViewById(R.id.docName);
        Name.setText(name);
        Log.d("DoctorMainActivity",user_id);


        /*Update doctor online status for e-visit   */
        //UpdateStatus("offline");

    }

    public void UpdateStatus(String status) {




        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "update_doctor_online_status.php?" +
                "doctor_id=" + user_id +
                "&status=" + status,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            //  Log.d("ApiResponse", response);

                            try {

                                //    JSONArray parent = new JSONArray(response);

                                JSONObject object = new JSONObject(response);

                                response = object.getString("success");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                        if (response.equals("1")) {

                            // PatientProfileActivity.getInstance().UpdateSuccessful();
                        } else {

                            final Dialog dialog = new Dialog(DoctorMainActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.text_dialog_ok);

                            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                            text.setText("Error! Status did not changed properly");

                            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                   //finish();
                                }
                            });

                            dialog.show();

                            Window window = dialog.getWindow();
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        }


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }


    private void setUpNavigation() {

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        Intent i = new Intent(DoctorMainActivity.this, DoctorProfileActivity.class);
                        // i.putExtra("studentInfo", studentList);
                        startActivity(i);
                        break;

                    case R.id.nav_web:

                        Uri uriUrl = Uri.parse("http://medical.suunnoo.com/");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                        break;


                    case R.id.nav_logout:
                        sessionManager.logout();
                        break;


                    case R.id.nav_exit:

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorMainActivity.this, R.style.DialogTheme)
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
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    public void OpenDoctorProfile(View view) {
        startActivity(new Intent(DoctorMainActivity.this, DoctorProfileActivity.class));
    }

    public void OpenDoctorAppointment(View view) {
        startActivity(new Intent(DoctorMainActivity.this, DoctorAppointmentsActivity.class));

    }

    public void OpenPharmacy(View view) {
        startActivity(new Intent(DoctorMainActivity.this, PharmacyActivity.class));

    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    public void OpenLabs(View view) {
        startActivity(new Intent(getApplicationContext(), LabsActivity.class));

    }

    public void OpenSchedule(View view) {
        startActivity(new Intent(getApplicationContext(), DoctorCalendarScheduleActivity.class));

    }

    public void OpenWaitingRoom(View view) {
        startActivity(new Intent(getApplicationContext(), WaitingRoomActivity.class));

    }

    public void OpenPatientsList(View view) {
        startActivity(new Intent(getApplicationContext(), DoctorsPatientListActivity.class));
    }

    public void OpenInbox(View view) {
        startActivity(new Intent(getApplicationContext(), InboxActivity.class));

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorMainActivity.this, R.style.DialogTheme)
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
    }

    public void OpenComingSoon(View view) {
        startActivity(new Intent(DoctorMainActivity.this, ComingSoonActivity.class));

    }

    public void OpenSubstanceAbuse(View view) {
        startActivity(new Intent(DoctorMainActivity.this, SubstanceAbuseActivity.class));

    }

    public void OpenImaging(View view) {
        startActivity(new Intent(DoctorMainActivity.this, ImagingActivity.class));

    }
}