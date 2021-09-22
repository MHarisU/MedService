package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView editText_password, editText_password_c, editText_password_current;


    ProgressDialog progressDialog;

    String newPassword;

    String user_id;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        progressDialog = new ProgressDialog(ChangePasswordActivity.this);

        editText_password_current = findViewById(R.id.editText_password_current);
        editText_password = findViewById(R.id.editText_password);
        editText_password_c = findViewById(R.id.editText_password_c);



        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        // name = user.get(sessionManager.FIRST_NAME);
        //  name = name + " " + user.get(sessionManager.LAST_NAME);
        //  email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);

    }

    public void Finish(View view) {
        finish();
    }

    public void ChangePassword(View view) {

        if ( editText_password.getText().toString().equals(editText_password_c.getText().toString())
                && !editText_password.getText().toString().equals("")
                && editText_password_current.getText().toString().equals(new SessionManager(ChangePasswordActivity.this).getPassword())
        ){

            newPassword = editText_password.getText().toString();


            progressDialog.setMessage("Changing Password...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            ChangePasswordOnline(newPassword);

            /*
            ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "/update_password.php?" +
                    "id=" +user_id+
                    "&password=" +newPassword,
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


                            if (response.equals("1")){
                                progressDialog.dismiss();

                                final Dialog dialog = new Dialog(ChangePasswordActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Password Changed");

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
                               // PatientProfileActivity.getInstance().UpdateSuccessful();
                            }else {
                                progressDialog.dismiss();

                                final Dialog dialog = new Dialog(ChangePasswordActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Password not changed");

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



                        }

                    });

            // asyncTask.execute();
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

        }
        else {
            Toast.makeText(this, "Enter Password Correctly", Toast.LENGTH_SHORT).show();
        }

    }


    private void ChangePasswordOnline(final String newPassword) {

        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("password", newPassword);

            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = orderJsonObject.toString();
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, new GlobalUrlApi().getNewBaseUrl() +
                "updateUserProfile/"+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("order_api_response", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            //JSONObject jsonData = jsonResponse.getJSONObject("Data");
                            String jsonStatus = jsonResponse.getString("Status");

                            if (jsonStatus.equals("True")) {

                                progressDialog.dismiss();
                                new SessionManager(ChangePasswordActivity.this).setPassword(newPassword);

                                final Dialog dialog = new Dialog(ChangePasswordActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Password Successfully Changed");

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


                            } else {

                                progressDialog.dismiss();
                                final Dialog dialog = new Dialog(ChangePasswordActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Password Not Changed");

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


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //   login_button.setVisibility(View.VISIBLE);
                            //   progress_bar.setVisibility(View.GONE);
                            // Toast.makeText(LoginActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(ChangePasswordActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
                            // progressDialog.dismiss();
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
                        //Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        //Login(email, password);
                        //    login_text.setVisibility(View.VISIBLE);
                        //   login_text.setText("Error from php");

                        //  login_button.setVisibility(View.VISIBLE);
                        // progress_bar.setVisibility(View.GONE);
                        //progressDialog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePasswordActivity.this, R.style.DialogTheme)
                                .setTitle("Warning!")
                                .setMessage("Volley Error")
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //  login_button.setVisibility(View.VISIBLE);
                                        //  progress_bar.setVisibility(View.GONE);

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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Bearer " + new SessionManager(ChangePasswordActivity.this).getToken();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}