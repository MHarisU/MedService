package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.med.medservice.Utils.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SplashActivity extends AppCompatActivity {


    ProgressBar bar;
    Handler handler = new Handler();

    String name, user_id, role;
    SessionManager sessionManager;


    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String uniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        AskPermission();


    }

    private void AskPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.USE_BIOMETRIC,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS
                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {

                    Granted2();

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    token.continuePermissionRequest();

                }
            }).check();
        }
        else {
            Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS
                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {

                    Granted2();

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    token.continuePermissionRequest();

                }
            }).check();
        }
    }

    private void Granted2() {


        uniqueId = UUID.randomUUID().toString();
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        //   reference.setValue(uniqueId);


        sessionManager = new SessionManager(this);
        // sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        role = user.get(sessionManager.USER_TYPE);
        user_id = user.get(sessionManager.ID);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sessionManager.isLogin()) {

                    finish();
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    intent.putExtra("checkActivity", "fronSplash");
                    startActivity(intent);

                } else {
                    finish();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    // Toast.makeText(SplashActivity.this, "Start", Toast.LENGTH_SHORT).show();
                }


            }
        }, 3000);

    }

    private void Granted() {


        uniqueId = UUID.randomUUID().toString();
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        //   reference.setValue(uniqueId);


        sessionManager = new SessionManager(this);
        // sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        role = user.get(sessionManager.USER_TYPE);
        user_id = user.get(sessionManager.ID);


        bar = findViewById(R.id.splash_progress);
        new Thread(new Runnable() {

            int i = 0;
            int progressStatus = 0;

            public void run() {
                while (progressStatus < 200) {
                    progressStatus += doWork();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        public void run() {
                            bar.setProgress(progressStatus);
                            i++;
                        }
                    });
                }
            }

            private int doWork() {

                return i + 1;
            }

        }).start();


        final LottieAnimationView view = findViewById(R.id.animation_view);

        view.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {


                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (sessionManager.isLogin()) {

                            finish();
                            Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                            intent.putExtra("checkActivity", "fronSplash");
                            startActivity(intent);

                        } else {
                            finish();
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            // Toast.makeText(SplashActivity.this, "Start", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, 3000);


            }

            @Override
            public void onAnimationEnd(Animator animator) {


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}