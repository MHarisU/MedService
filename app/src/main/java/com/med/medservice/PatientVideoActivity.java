package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.med.medservice.Models.OnlineDoctors.OnlineDoctorsList;
import com.med.medservice.Utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;

public class PatientVideoActivity extends AppCompatActivity{



    String name, user_id, role, user_email;
    SessionManager sessionManager;

    WebView myWebView;

    private String TAG = "TEST";
    private PermissionRequest mPermissionRequest;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String[] PERM_CAMERA =
            {Manifest.permission.CAMERA};


    FirebaseDatabase rootNode;
    DatabaseReference reference;


    OnlineDoctorsList currentData;
    boolean checkCallStarted = false;

    CountDownTimer downTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_video);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        role = user.get(sessionManager.USER_TYPE);
        user_id = user.get(sessionManager.ID);
        user_email = user.get(sessionManager.EMAIL);


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        Intent intent = getIntent();
        currentData = (OnlineDoctorsList) intent.getSerializableExtra("selectedDoctor");


        myWebView = (WebView) findViewById(R.id.webView1);
//chromeProgressBar = (ProgressBar) findViewById(R.id.progressBarChrome);
        //Settings
        WebSettings webSettings = myWebView.getSettings();

        myWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);

        //webSettings.setLoadsImagesAutomatically(true);
        //inizialize client

        //load website by URL

        myWebView.loadUrl("https://umbrellamd.com/video_chat/pages/r.html?room=tcgij5htuo&p=eyJsc1JlcFVybCI6Imh0dHBzOi8vdW1icmVsbGFtZC5jb20vdmlkZW9fY2hhdC8iLCJkaXNhYmxlVmlkZW8iOjAsImRpc2FibGVBdWRpbyI6MCwiZGlzYWJsZVNjcmVlblNoYXJlIjoxLCJkaXNhYmxlV2hpdGVib2FyZCI6MCwiZGlzYWJsZVRyYW5zZmVyIjoxLCJhdXRvQWNjZXB0VmlkZW8iOjEsImF1dG9BY2NlcHRBdWRpbyI6MX0");
        //register token for notification

        // this.onStart();
       // chromeProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        myWebView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam

            @Override
            public void onPermissionRequest(final PermissionRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

                        List<String> permissions = new ArrayList<String>();

                        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                            permissions.add(Manifest.permission.CAMERA);

                        }
                        if (!permissions.isEmpty()) {
                            requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
                        }
                    }

                }
            }
        });

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);
                super.onPageFinished(view, url);
                if (url.equals("https://suunnoo.com/pages/1")){

                    final Dialog dialog = new Dialog(PatientVideoActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.text_dialog_ok);

                    TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                    text.setText("Video call session with doctor is ended");

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                    dialog.show();

                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                }
                else {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            startTimer();
                        }
                    }, 0000);

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        CheckIfSessionEnded();

    }

    private void startTimer() {

        final TextView timerView = findViewById(R.id.timerView);

        downTimer = new CountDownTimer(357000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerView.setText(""+String.format("Remaining %dm : %ds",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timerView.setText("done!");
            }
        };

        downTimer.start();

    }

    private void CheckIfSessionEnded() {
        reference.child("calling").child(currentData.getDoctor_id()).child("isAvailable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    if (snapshot.getValue().toString() == "false") {

                        if (!checkCallStarted) {
                            checkCallStarted = true;
                            reference.child("calling").child(currentData.getDoctor_id()).setValue(null);
                            EndSession();
                            //startActivity(new Intent(SendInvitationActivity.this, PatientVideoActivity.class));
                            //finish();
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void EndSession(View view) {
        myWebView.loadUrl("https://suunnoo.com/pages/1");
        final Dialog dialog = new Dialog(PatientVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.text_dialog_ok);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText("Video call session with doctor is ended");

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void EndSession() {
        myWebView.loadUrl("https://suunnoo.com/pages/1");
        final Dialog dialog = new Dialog(PatientVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.text_dialog_ok);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText("Video call session with doctor is ended");

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });


        try {
            dialog.show();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /*
    @Override
    public void onBackPressed()
    {
        if(myWebView.canGoBack())
            myWebView.goBack();
        else
            super.onBackPressed();
    }

     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downTimer.cancel();
    }
}