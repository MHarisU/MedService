package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.med.medservice.Utils.FirebaseUserModel;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText editText_email, editText_password;

    ProgressBar progress_bar;
    Button login_button;

    private static String URL_Login;
    GlobalUrlApi globalUrlApi;
    //  SessionManagerPatient sessionManager;
    SessionManager sessionManager;


    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        progress_bar = findViewById(R.id.progress_bar);
        login_button = findViewById(R.id.login_button);

        globalUrlApi = new GlobalUrlApi();
        URL_Login = globalUrlApi.getUrlAppFolder() + "login.php";


        sessionManager = new SessionManager(this);
    }

    public void LoginButtonClick(View view) {


        progress_bar.setVisibility(View.VISIBLE);
        login_button.setVisibility(View.GONE);

        final String email = editText_email.getText().toString();
        final String password = editText_password.getText().toString();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (email != null && !email.equals("") && password != null && !password.equals("")) {


                    Login(email, password);

                } else {
                    Toast.makeText(LoginActivity.this, "Please enter Email and Password.", Toast.LENGTH_SHORT).show();

                    progress_bar.setVisibility(View.GONE);
                    login_button.setVisibility(View.VISIBLE);
                }
            }
        }, 10);

        /*
        EditText editText_email = findViewById(R.id.editText_email);
        if (editText_email.getText().toString().equals("pat@test.com")){
            startActivity(new Intent(LoginActivity.this, PatientMainActivity.class));
            finish();

        }
        else if (editText_email.getText().toString().equals("doc@test.com")){
            startActivity(new Intent(LoginActivity.this, DoctorMainActivity.class));
            finish();
        }
        else {
            Toast.makeText(this, "Enter correct email", Toast.LENGTH_SHORT).show();
        }

        */
    }


    private void Login(final String email, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id").trim();
                                    String first_name = object.getString("name").trim();
                                    String last_name = object.getString("last_name").trim();
                                    String email = object.getString("email").trim();
                                    String user_type = object.getString("user_type").trim();
                                    String phone = object.getString("phone_number").trim();

                                    sessionManager.createSession(id, first_name, last_name, email, password, user_type, phone);

                                    String android_id = Settings.Secure.getString(getContentResolver(),
                                            Settings.Secure.ANDROID_ID);

                                    rootNode = FirebaseDatabase.getInstance();
                                    reference = rootNode.getReference("users");

                                    FirebaseUserModel userModel = new FirebaseUserModel(id, first_name+" "+last_name, email, android_id);
                                    reference.child(id).setValue(userModel);

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

                                    login_button.setVisibility(View.VISIBLE);
                                    progress_bar.setVisibility(View.GONE);

                                }

                            } else if (success.equals("0")) {

                                login_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("Incorrect Password")
                                        .setCancelable(false)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                login_button.setVisibility(View.VISIBLE);
                                                progress_bar.setVisibility(View.GONE);

                                            }
                                        });
                                //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                dialog.show();


                            } else if (success.equals("2")) {

                                login_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("Incorrect Email ")
                                        .setCancelable(false)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                login_button.setVisibility(View.VISIBLE);
                                                progress_bar.setVisibility(View.GONE);

                                            }
                                        });
                                //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                dialog.show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            login_button.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);
                            // Toast.makeText(LoginActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                            //  login_text.setVisibility(View.VISIBLE);
                            // login_text.setText("JSON Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  login_button.setVisibility(View.VISIBLE);
                        //  progress_bar.setVisibility(View.GONE);
                        //  Toast.makeText(LoginActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "Trying login please wait", Toast.LENGTH_SHORT).show();
                        Login(email, password);
                        //    login_text.setVisibility(View.VISIBLE);
                        //   login_text.setText("Error from php");

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
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

    public void OpenRegisterChooser(View view) {
         startActivity(new Intent(getApplicationContext(), ChooseRegisterActivity.class));

    }

}