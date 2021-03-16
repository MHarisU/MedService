package com.med.medservice.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.FirebaseDatabase;
import com.med.medservice.DoctorMainActivity;
import com.med.medservice.LoginActivity;
import com.med.medservice.PatientMainActivity;
import com.med.medservice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ApiTokenCaller {

    String URL_Link;
    Context mContext;


    public interface AsyncApiResponse {
        void processFinish(String response);
    }

    public AsyncApiResponse delegate = null;

    public ApiTokenCaller(Context mContext, String URL_Link, ApiTokenCaller.AsyncApiResponse delegate){
        this.mContext = mContext;
        this.delegate = delegate;
        this.URL_Link = URL_Link;

        callApi();
    }


    private void callApi() {





        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_Link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        delegate.processFinish(response);

/*
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            JSONObject jsonData = jsonResponse.getJSONObject("data");
                            String jsonToken = jsonResponse.getString("token");
                            String jsonStatus = jsonResponse.getString("status");

                            if (jsonStatus.equals("logged_inn")){


                                String id = jsonData.getString("id").trim();
                                String first_name = jsonData.getString("name").trim();
                                String last_name = jsonData.getString("last_name").trim();
                                String email = jsonData.getString("email").trim();
                                String user_type = jsonData.getString("user_type").trim();
                                String phone = jsonData.getString("phone_number").trim();

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
                            else  {

                                login_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("Incorrect Email or Password")
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
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  login_button.setVisibility(View.VISIBLE);
                        //  progress_bar.setVisibility(View.GONE);
                        //  Toast.makeText(LoginActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("api_error", error.toString());
                        //Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        //Login(email, password);
                        //    login_text.setVisibility(View.VISIBLE);
                        //   login_text.setText("Error from php");

//                        login_button.setVisibility(View.VISIBLE);
//                        progress_bar.setVisibility(View.GONE);
//                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this, R.style.DialogTheme)
//                                .setTitle("Warning!")
//                                .setMessage("Incorrect Email or Password")
//                                .setCancelable(false)
//                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        login_button.setVisibility(View.VISIBLE);
//                                        progress_bar.setVisibility(View.GONE);
//
//                                    }
//                                });
//                        //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
//                        dialog.show();

                    }
                }) {


/*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + new SessionManager(mContext).getToken());
                return headerMap;
            }*/

         /*   @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String> ();
                //TokenService tokenservice = new TokenService(ctx);
                String accesstoken = new SessionManager(mContext).getToken();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }*/

            //This is for Headers If You Needed
           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + new SessionManager(mContext).getToken());
                return params;
            }*/

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer "  + new SessionManager(mContext).getToken());
                return headers;
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Bearer " + new SessionManager(mContext).getToken();
                headers.put("Authorization", auth);
                return headers;
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

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }


}
