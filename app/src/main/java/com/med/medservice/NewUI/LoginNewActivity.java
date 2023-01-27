package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.med.medservice.BookAppointmentActivity;
import com.med.medservice.DoctorMainActivity;
import com.med.medservice.LoginActivity;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.PatientMainActivity;
import com.med.medservice.R;
import com.med.medservice.Utils.FirebaseUserModel;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginNewActivity extends AppCompatActivity {

    Dialog dialog;
    TextInputEditText editTextEmail, editTextPassword;

    Button loginButton;
    ProgressBar progressBar;

    private static String URLLogin;
    GlobalUrlApi globalUrlApi;
    SessionManager sessionManager;

    FirebaseDatabase rootNode;
    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_new);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);

        globalUrlApi = new GlobalUrlApi();
        URLLogin = globalUrlApi.getNewBaseUrl() + "https://demo.umbrellamd-video.com/api/login";


        sessionManager = new SessionManager(this);


        setRegistrationDialog();



    }

    private void setRegistrationDialog() {

        dialog = new Dialog(LoginNewActivity.this);
        dialog.setContentView(R.layout.registration_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView textView = findViewById(R.id.registrationButton);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public void LoginButtonClick(View view) {
        finish();
        startActivity(new Intent(LoginNewActivity.this, DoctorMainNewActivity.class));

//        progressBar.setVisibility(View.VISIBLE);
//        loginButton.setVisibility(View.GONE);
//
//        final String email = editTextEmail.getText().toString();
//        final String password = editTextPassword.getText().toString();
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (email != null && !email.equals("") && password != null && !password.equals("")) {
//
//                    Login(email, password);
//
//                } else {
//                    Toast.makeText(LoginNewActivity.this, "Please enter Email and Password.", Toast.LENGTH_SHORT).show();
//
//                    progressBar.setVisibility(View.GONE);
//                    loginButton.setVisibility(View.VISIBLE);
//                }
//            }
//        }, 10);

    }

    private void Login(final String email, final String password) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("sign_api_response", response);

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }

                        try {
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            String jsonStatus = jsonResponse.getString("Status");

                            if (jsonStatus.equals("logged_inn")) {
                                JSONObject jsonData = jsonResponse.getJSONObject("Data");
                                String jsonToken = jsonResponse.getString("Token");

                                String id = jsonData.getString("id").trim();
                                String first_name = jsonData.getString("name").trim();
                                String last_name = jsonData.getString("last_name").trim();
                                String email = jsonData.getString("email").trim();
                                String user_type = jsonData.getString("user_type").trim();
                                String phone = jsonData.getString("phone_number").trim();
                                String date_of_birth = jsonData.getString("date_of_birth").trim();
                                String office_address = jsonData.getString("office_address").trim();

                                sessionManager.createSession(id, first_name, last_name, email, password, user_type, phone, jsonToken, date_of_birth, office_address);

                                String android_id = Settings.Secure.getString(getContentResolver(),
                                        Settings.Secure.ANDROID_ID);

                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("users");

                                FirebaseUserModel userModel = new FirebaseUserModel(id, first_name + " " + last_name, email, android_id);
                                reference.child(id).setValue(userModel);
                                Log.d("firebase_info", id + " " + first_name + " " + last_name + " " + email + " " + android_id);

                                //   Toast.makeText(LoginActivity.this, id + "\n" + first_name + "\n" + last_name + "\n" + email + "\n" + user_type + "\n" + phone, Toast.LENGTH_LONG).show();

                                if (user_type.equals("doctor")) {

                                    Intent intent = new Intent(getApplicationContext(), DoctorMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (user_type.equals("patient")) {

                                    Intent intent = new Intent(getApplicationContext(), PatientMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                //  Toast.makeText(LoginActivity.this, name+"\n"+id, Toast.LENGTH_SHORT).show();

                                loginButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                            } else if (jsonStatus.equals("not_allowed")) {
                                loginButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginNewActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("This account is not approved yet")
                                        .setCancelable(false)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                loginButton.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        });
                                //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                dialog.show();
                            } else {

                                loginButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginNewActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("Incorrect Email or Password")
                                        .setCancelable(false)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                loginButton.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        });
                                //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                dialog.show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            // Toast.makeText(LoginActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(LoginActivity.this, "Api not responding.", Toast.LENGTH_SHORT).show();
                            //  login_text.setVisibility(View.VISIBLE);
                            // login_text.setText("JSON Error");

                            try {
                                checkException(jsonObject);
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  login_button.setVisibility(View.VISIBLE);
                        //  progress_bar.setVisibility(View.GONE);
                        //  Toast.makeText(LoginActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        //Login(email, password);
                        //    login_text.setVisibility(View.VISIBLE);
                        //   login_text.setText("Error from php");

                        loginButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginNewActivity.this, R.style.DialogTheme)
                                .setTitle("Warning!")
                                .setMessage("Incorrect Email or Password")
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        loginButton.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });
                        //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                        dialog.show();

                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0; //retry turn off
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void checkException(JSONObject jsonObject) throws JSONException {
        JSONArray message = jsonObject.getJSONArray("message");
        Log.d("loginActivity", message.toString());
        if (message.toString().equals("These credentials do not match our records.")){

            loginButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginNewActivity.this, R.style.DialogTheme)
                    .setTitle("Warning!")
                    .setMessage("Incorrect Email or Password")
                    .setCancelable(false)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            loginButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }
                    });
            //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
            dialog.show();
        }else {
            loginButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginNewActivity.this, R.style.DialogTheme)
                    .setTitle("Warning!")
                    .setMessage("Server not responding, please try again later.")
                    .setCancelable(false)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            loginButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        }
                    });
            //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
            dialog.show();
        }
    }






    public void OpenPatientDashboard(View view) {
        startActivity(new Intent(LoginNewActivity.this, PatientMainNewActivity.class));
    }

    public void openDoctorRegistration(View view) {
        finish();
        startActivity(new Intent(LoginNewActivity.this, RegisterDoctorNewActivity.class));


    }

    public void openPatientRegistration(View view) {
        finish();
        startActivity(new Intent(LoginNewActivity.this, RegisterPatientNewActivity.class));


    }

    public void backToLogin(View view) {
        dialog.dismiss();
    }

    public void close(View view) {
        dialog.dismiss();
    }
}