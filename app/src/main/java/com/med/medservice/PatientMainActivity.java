package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.med.medservice.Utils.ApiTokenCaller;
import com.med.medservice.Utils.AuthService;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.MyCountDownTimer;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.UpdateCartInterface;

import java.util.HashMap;

public class PatientMainActivity extends AppCompatActivity implements UpdateCartInterface {


    String name, user_id, email;
    SessionManager sessionManager;

    DrawerLayout drawerLayout;
    NavigationView navigationView;


    CartDBHelper mydb;
    TextView cartNumberView;

   // AuthAsyncTask authAsyncTask;

    Boolean checkAuthQueue = false;

    MyCountDownTimer countDownTimer;
    private long startTime = 1* 20 * 1000; // 15 MINS IDLE TIME
    private final long interval = 1 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_main);

        //startService(new Intent(PatientMainActivity.this, AuthService.class));
        // StartAuthTask();

        countDownTimer = new MyCountDownTimer(this, startTime, interval);


        cartNumberView = findViewById(R.id.cartNumberView);
        mydb = new CartDBHelper(this);
        UpdateCart();

        setUpNavigation();


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);
        TextView Name;
        Name = findViewById(R.id.patName);
        Name.setText(name);

        /*new ApiTokenCaller(PatientMainActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProductParentCategories?id=3",
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {
                        Log.d("token_api_response", response);

                    }
                }
        );*/


    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        //Reset the timer on user interaction...
        countDownTimer.cancel();
        countDownTimer.start();
    }

    private void StartAuthTask() {

        checkAuthQueue = true;
      //  authAsyncTask = new AuthAsyncTask();
     //   authAsyncTask.execute();

    }

    private void setUpNavigation() {

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        Intent i = new Intent(PatientMainActivity.this, PatientProfileActivity.class);
                        // i.putExtra("studentInfo", studentList);
                        startActivity(i);
                        break;

                    case R.id.nav_web:

                        Uri uriUrl = Uri.parse("http://medical.suunnoo.com/");
                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                        startActivity(launchBrowser);
                        break;


                    case R.id.nav_logout:
                        sessionManager.logoutPatient();
                        break;


                    case R.id.nav_exit:

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(PatientMainActivity.this, R.style.DialogTheme)
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

                                     //   if (authAsyncTask != null)
                                     //       authAsyncTask.cancel(true);

                                        stopService(new Intent(PatientMainActivity.this, AuthService.class));
                                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(homeIntent);
                                        android.os.Process.killProcess(android.os.Process.myPid());
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

    public void OpenPatientProfile(View view) {
        startActivity(new Intent(PatientMainActivity.this, PatientProfileActivity.class));

    }

    public void OpenPatientAppointments(View view) {
        startActivity(new Intent(PatientMainActivity.this, PatientAppointmentsActivity.class));

    }

    public void OpenPharmacy(View view) {
        startActivity(new Intent(PatientMainActivity.this, PharmacyActivity.class));

    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    public void OpenLabs(View view) {
        startActivity(new Intent(getApplicationContext(), LabsActivity.class));

    }

    public void OpenEvisit(View view) {
        startActivity(new Intent(getApplicationContext(), EvisitActivity.class));
    }

    public void OpenSession(View view) {
        startActivity(new Intent(getApplicationContext(), SessionActivity.class));

    }

    public void OpenInbox(View view) {
        startActivity(new Intent(getApplicationContext(), InboxActivity.class));

    }


    @Override
    protected void onResume() {
      //  if (!checkAuthQueue) {
//            authAsyncTask.cancel(true);
      //      StartAuthTask();
   //     }


        countDownTimer.start();
        super.onResume();
        UpdateCart();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(PatientMainActivity.this, R.style.DialogTheme)
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

                   //     if (authAsyncTask != null)
                   //         authAsyncTask.cancel(true);

                        stopService(new Intent(PatientMainActivity.this, AuthService.class));
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        finish();
                        System.exit(0);
                    }
                });
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();

      //  if (authAsyncTask != null)
        //    authAsyncTask.cancel(true);
    }

    @Override
    public void UpdateCart() {

        if (mydb.numberOfRows() > 0) {
            cartNumberView.setText("" + mydb.numberOfRows());
        } else {
            cartNumberView.setText("");
        }
    }


    private class AuthAsyncTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            if (!isCancelled()) {
// Do your stuff
            }
            return "resp";
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming


            if (!isCancelled()) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startService(new Intent(PatientMainActivity.this, AuthService.class));
                        checkAuthQueue = false;
                    }
                }, 20000);
            }


        }


    }
}