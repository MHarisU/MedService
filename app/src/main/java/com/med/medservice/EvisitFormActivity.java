package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.med.medservice.NetworkAPI.ApiCallerNew;
import com.med.medservice.NetworkAPI.ApiPostCall;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Diaglogs.SymptomSelectorDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EvisitFormActivity extends AppCompatActivity implements SymptomSelectorDialogFragment.onMultiChoiceListener {

    TextView symtoms_text;

    OnlineDoctorsList currentData;
    String headache = "0";
    String fever = "0";
    String flu = "0";
    String nausea = "0";
    String other = "0";

    TextView patDescription;

    String name, user_id, role, user_email;
    SessionManager sessionManager;

    String symptoms_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_evisit_form);

        GetUserFromManager();

        patDescription = findViewById(R.id.patDescription);

        Intent intent = getIntent();
        currentData = (OnlineDoctorsList) intent.getSerializableExtra("selectedDoctor");
        //Toast.makeText(this, ""+currentData.getDoctor_name(), Toast.LENGTH_SHORT).show();
        TextView DoctorNameEvisit = findViewById(R.id.DoctorNameEvisit);
        DoctorNameEvisit.setText("E-Visit with " + currentData.getDoctor_name());
        symtoms_text = findViewById(R.id.symtoms_text);

    }

    private void GetUserFromManager() {

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        role = user.get(sessionManager.USER_TYPE);
        user_id = user.get(sessionManager.ID);
        user_email = user.get(sessionManager.EMAIL);

    }

    public void Close(View view) {
        finish();
    }

    public void SelectSymptoms(View view) {
        DialogFragment symptomsDialog = new SymptomSelectorDialogFragment();
        symptomsDialog.setCancelable(false);
        symptomsDialog.show(getSupportFragmentManager(), "Multichoice Dialog");
    }


    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedSymptoms) {
        headache = "0";
        fever = "0";
        flu = "0";
        nausea = "0";
        other = "0";


        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilders = new StringBuilder();
        stringBuilder.append("Selected Symptoms:");
        for (String str : selectedSymptoms) {
            stringBuilder.append("\n- " + str);
            stringBuilders.append(str + ",");

            if (str.equals("Headache")) {
                headache = "1";
            }
            if (str.equals("Fever")) {
                fever = "1";
            }
            if (str.equals("Flu")) {
                flu = "1";
            }
            if (str.equals("Nausea")) {
                nausea = "1";
            }
            if (str.equals("Other")) {
                other = "1";
            }

        }
        //SelectedSymp = stringBuilders.toString();
        symtoms_text.setText(stringBuilder);

    }

    @Override
    public void onNegativeButtonClicked() {

        symtoms_text.setText("Select Symptoms");
    }

    public void Submit(View view) {

        if (symtoms_text.getText().toString() != null && !symtoms_text.getText().toString().equals("")) {

            TextView submitButton = findViewById(R.id.submitButton);
            submitButton.setVisibility(View.GONE);

            String desc = patDescription.getText().toString();

            CreateSymptoms(desc);


        } else {
            Toast.makeText(this, "Please select symptoms", Toast.LENGTH_SHORT).show();
        }

    }

    private void CreateSymptoms(final String desc) {



        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("patient_id", user_id);
            orderJsonObject.put("doctor_id", currentData.getDoctor_id());
            orderJsonObject.put("headache", headache);
            orderJsonObject.put("flu", flu);
            orderJsonObject.put("fever", fever);
            orderJsonObject.put("nausea", nausea);
            orderJsonObject.put("others", other);
            orderJsonObject.put("description", desc);
            orderJsonObject.put("status", "pending");

            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = orderJsonObject.toString();

        new ApiPostCall(
                EvisitFormActivity.this,
                new GlobalUrlApi().getNewBaseUrl() + "createSymptomsForSession",
                requestBody,
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {


                        Log.d("order_api_response", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            JSONObject jsonData = jsonResponse.getJSONObject("Data");
                            String jsonStatus = jsonResponse.getString("Status");

                            if (jsonStatus.equals("True")) {

                                symptoms_id = jsonData.getString("id");
                                //Toast.makeText(EvisitFormActivity.this, "" + symptoms_id, Toast.LENGTH_LONG).show();


                                Intent intent = new Intent(EvisitFormActivity.this, CreditCardPaymentActivity.class);
                                intent.putExtra("symptoms_id", symptoms_id);
                                intent.putExtra("desc", desc);
                                intent.putExtra("selectedDoctor", currentData);
                                startActivity(intent);

                                //startActivity(new Intent(EvisitFormActivity.this, CreditCardPaymentActivity.class));
                                finish();


                            } else {

                                // progressDialog.dismiss();
                                final Dialog dialog = new Dialog(EvisitFormActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Symptoms not created");

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
                            Toast.makeText(EvisitFormActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();

/*

        StringRequest stringRequest = new StringRequest(Request.Method.POST, new GlobalUrlApi().getNewBaseUrl() +
                "createSymptomsForSession",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("order_api_response", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            JSONObject jsonData = jsonResponse.getJSONObject("Data");
                            String jsonStatus = jsonResponse.getString("Status");

                            if (jsonStatus.equals("True")) {

                                symptoms_id = jsonData.getString("id");
                                //Toast.makeText(EvisitFormActivity.this, "" + symptoms_id, Toast.LENGTH_LONG).show();


                                Intent intent = new Intent(EvisitFormActivity.this, CreditCardPaymentActivity.class);
                                intent.putExtra("symptoms_id", symptoms_id);
                                intent.putExtra("desc", desc);
                                intent.putExtra("selectedDoctor", currentData);
                                startActivity(intent);

                                //startActivity(new Intent(EvisitFormActivity.this, CreditCardPaymentActivity.class));
                                finish();


                            } else {

                                // progressDialog.dismiss();
                                final Dialog dialog = new Dialog(EvisitFormActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Symptoms not created");

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
                            Toast.makeText(EvisitFormActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EvisitFormActivity.this, R.style.DialogTheme)
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
                String auth = "Bearer " + new SessionManager(EvisitFormActivity.this).getToken();
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
*/

    }


    private void CreateSymptoms2(final String desc) {

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "create_symptoms.php?" +
                "patient_id=" + user_id +
                "&doctor_id=" + currentData.getDoctor_id() +
                "&headache=" + headache +
                "&fever=" + fever +
                "&flu=" + flu +
                "&nausea=" + nausea +
                "&others=" + other +
                "&description=" + desc
                ,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            // Log.d("ApiResponse", response);

                            try {

                                JSONArray parent = new JSONArray(response);
                                // doctorNames = new String[parent.length()];
                                //  doctorId = new String[parent.length()];

                                // doctorNames[0] = "Select Doctor";
                                //  doctorId[0] = "";

                                for (int i = 0; i <= parent.length(); i++) {
                                    JSONObject child = parent.getJSONObject(i);
                                    symptoms_id = child.getString("id");
                                    //Toast.makeText(EvisitFormActivity.this, "" + symptoms_id, Toast.LENGTH_LONG).show();


                                    Intent intent = new Intent(EvisitFormActivity.this, CreditCardPaymentActivity.class);
                                    intent.putExtra("symptoms_id", symptoms_id);
                                    intent.putExtra("desc", desc);
                                    intent.putExtra("selectedDoctor", currentData);
                                    startActivity(intent);

                                    //startActivity(new Intent(EvisitFormActivity.this, CreditCardPaymentActivity.class));
                                    finish();
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}