package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.med.medservice.Models.OnlineDoctors.OnlineDoctorsList;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CreditCardPaymentActivity extends AppCompatActivity {

    OnlineDoctorsList currentData;
    String symptoms_id, desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_credit_card_payment);


        Intent intent = getIntent();
        currentData = (OnlineDoctorsList) intent.getSerializableExtra("selectedDoctor");
        symptoms_id = intent.getStringExtra("symptoms_id");
        desc = intent.getStringExtra("desc");
    }

    public void Close(View view) {
        finish();
    }

    public void ProcessPayment(View view) {

//        Intent intent = new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class);
//        intent.putExtra("symptoms_id", symptoms_id);
//        intent.putExtra("desc", desc);
//        intent.putExtra("selectedDoctor", currentData);
//        startActivity(intent);
//
//        //startActivity(new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class));
//        finish();

        ProcessPaymentOnline();
    }

    private void ProcessPaymentOnline() {


        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("symptom_id", symptoms_id);
            orderJsonObject.put("patient_id", new SessionManager(CreditCardPaymentActivity.this).getUserId());
            orderJsonObject.put("doctor_id", currentData.getDoctor_id());

            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = orderJsonObject.toString();
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, new GlobalUrlApi().getNewBaseUrl() +
                "createPaymentForSession",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("order_api_response", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                         //   JSONObject jsonData = jsonResponse.getJSONObject("Data");
                            String SessionID = jsonResponse.getString("SessionID");
                            String jsonStatus = jsonResponse.getString("Status");

                            if (jsonStatus.equals("True")) {

                                Toast.makeText(CreditCardPaymentActivity.this, ""+SessionID, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class);
                                intent.putExtra("session_id", SessionID);
                                intent.putExtra("symptoms_id", symptoms_id);
                                intent.putExtra("desc", desc);
                                intent.putExtra("selectedDoctor", currentData);
                                startActivity(intent);

                                //startActivity(new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class));
                                finish();


                            } else {

                                // progressDialog.dismiss();
                                final Dialog dialog = new Dialog(CreditCardPaymentActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("payment not successful");

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
                            Toast.makeText(CreditCardPaymentActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(CreditCardPaymentActivity.this, R.style.DialogTheme)
                                .setTitle("Warning!")
                                .setMessage("Volley Error\n"+error.toString())
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
                String auth = "Bearer " + new SessionManager(CreditCardPaymentActivity.this).getToken();
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