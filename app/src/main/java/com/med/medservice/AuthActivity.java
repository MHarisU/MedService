package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.med.medservice.Utils.AuthService;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.ViewDialog;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class AuthActivity extends AppCompatActivity {


    String name, user_id, email, role, password;
    SessionManager sessionManager;

    EditText PasswordInputView;
    TextView NameView;

    String checkActivity;

    BiometricManager biometricManager;

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_auth);

        SetupBioMetric();

        Intent intent = getIntent();
        checkActivity = intent.getStringExtra("checkActivity");


        sessionManager = new SessionManager(this);
        if (sessionManager.isLogin()) {
        } else {
            finish();
            startActivity(new Intent(AuthActivity.this, LoginActivity.class));
        }

        //  sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        role = user.get(sessionManager.USER_TYPE);
        password = user.get(sessionManager.PASSWORD);

        user_id = user.get(sessionManager.ID);

        NameView = findViewById(R.id.NameView);
        PasswordInputView = findViewById(R.id.PasswordInputView);
        NameView.setText(name);


    }

    private void SetupBioMetric() {
        biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
        }


        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(AuthActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(AuthActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (checkActivity.equals("fronSplash")) {

                    if (role.equals("patient")) {
                        finish();
                        startActivity(new Intent(AuthActivity.this, PatientMainActivity.class));
                    } else if (role.equals("doctor")) {
                        finish();
                        startActivity(new Intent(AuthActivity.this, DoctorMainActivity.class));
                    }

                } else {
                    finish();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Sign-in")
                .setDescription("User your fingerprint to sign-in to app")
                .setNegativeButtonText("Cancel")
                .build();
    }


    public void Authenticate(View view) {

        if (!PasswordInputView.getText().toString().equals("")) {
            if (password.equals(PasswordInputView.getText().toString())) {

                if (checkActivity.equals("fronSplash")) {

                    if (role.equals("patient")) {
                        finish();
                        startActivity(new Intent(AuthActivity.this, PatientMainActivity.class));
                    } else if (role.equals("doctor")) {
                        finish();
                        startActivity(new Intent(AuthActivity.this, DoctorMainActivity.class));
                    }

                } else {
                    finish();
                }


            } else {

                ViewDialog viewDialog = new ViewDialog();
                viewDialog.showDialog(AuthActivity.this, "Please enter correct password");
            }

        } else {
            ViewDialog viewDialog = new ViewDialog();
            viewDialog.showDialog(AuthActivity.this, "Please enter your password");
        }

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(AuthActivity.this, R.style.DialogTheme)
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

    public void UseBioMetric(View view) {


        biometricPrompt.authenticate(promptInfo);


    }

    public void CloseAuth(View view) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(AuthActivity.this, R.style.DialogTheme)
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
}