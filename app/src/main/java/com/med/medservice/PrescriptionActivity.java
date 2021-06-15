package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.med.medservice.Utils.DosageList;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.OpenPrescribedItems;
import com.med.medservice.Utils.PrescriptionIDList;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrescriptionActivity extends AppCompatActivity {

    EditText diagnosisView, notesView;
    String session_id;

    OpenPrescribedItems prescribedItems;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        SetupUI();

        prescribedItems = new OpenPrescribedItems(PrescriptionActivity.this, "PrescriptionActivity", session_id);

    }

    private void SetupUI() {
        diagnosisView = findViewById(R.id.diagnosisView);
        notesView = findViewById(R.id.notesView);

        Intent intent = getIntent();
        diagnosisView.setText(intent.getStringExtra("diagnosis"));
        notesView.setText(intent.getStringExtra("notes"));
        session_id = intent.getStringExtra("session_id");

    }

    public void Confirm(View view) {

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Confirming Final Prescription");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
// To dismiss the dialog

        AddDiagnosisAndNotes();
    }

    private void AddDiagnosisAndNotes() {


        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("provider_notes", notesView.getText().toString());
            orderJsonObject.put("diagnosis", diagnosisView.getText().toString());
            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = orderJsonObject.toString();
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, new GlobalUrlApi().getNewBaseUrl() +
                "updateDiagnosisAndNotes/" + session_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response_prescription", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            //   JSONObject jsonData = jsonResponse.getJSONObject("Data");
                            // String SessionID = jsonResponse.getString("SessionID");
                            String jsonStatus = jsonResponse.getString("Status");

                            if (jsonStatus.equals("True")) {


                                //startActivity(new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class));
                                Toast.makeText(PrescriptionActivity.this, "Notes Added", Toast.LENGTH_SHORT).show();

                                //prescribedItems = new OpenPrescribedItems(PrescriptionActivity.this, "AddItems", session_id);
                                prescribedItems.addPrescribeItems(new OpenPrescribedItems.AsyncApiResponse() {
                                    @Override
                                    public void processFinish(ArrayList<PrescriptionIDList> prescriptionIDLists, ArrayList<DosageList> dosageLists) {
                                        Log.d("response_prescription", "Prescription Items" + prescriptionIDLists.size());
                                        Log.d("response_prescription", "dosage Items" + dosageLists.size());
                                        insertDosageOnline(prescriptionIDLists, dosageLists);

                                    }
                                });


                            } else {

                                // progressDialog.dismiss();
                                final Dialog dialog = new Dialog(PrescriptionActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Server not responding");

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
                            Toast.makeText(PrescriptionActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PrescriptionActivity.this, R.style.DialogTheme)
                                .setTitle("Warning!")
                                .setMessage("Volley Error\n" + error.toString())
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
                String auth = "Bearer " + new SessionManager(PrescriptionActivity.this).getToken();
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

    private void insertDosageOnline(ArrayList<PrescriptionIDList> prescriptionIDLists, ArrayList<DosageList> dosageLists) {

        for (int i = 0; i < prescriptionIDLists.size(); i++) {
            PrescriptionIDList prescriptionIDList = prescriptionIDLists.get(i);
            for (int j = 0; j < dosageLists.size(); j++) {
                DosageList dosageList = dosageLists.get(j);
                if (dosageList.getItem_id() == prescriptionIDList.getItem_id()) {
                    dosageList.setPrescription_id(prescriptionIDList.getPrescription_id());
                    dosageLists.set(j, dosageList);
                }
            }
        }

        insertDosageNetworkCall(dosageLists);

    }

    private void insertDosageNetworkCall(ArrayList<DosageList> dosageLists) {



        /*JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("provider_notes", notesView.getText().toString());
            orderJsonObject.put("diagnosis", diagnosisView.getText().toString());
            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        final int[] check_response_size = {0};

        for (DosageList dosageList : dosageLists) {

            final String requestBody = dosageList.getDosageDetails();
            // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();


            StringRequest stringRequest = new StringRequest(Request.Method.PUT, new GlobalUrlApi().getNewBaseUrl() +
                    "updatePrescribedMedicines/" + dosageList.getPrescription_id(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("response_prescription", response);


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                                //   JSONObject jsonData = jsonResponse.getJSONObject("Data");
                                // String SessionID = jsonResponse.getString("SessionID");
                                String jsonStatus = jsonResponse.getString("Status");
                                String jsonMessage = jsonResponse.getString("Message");

                                if (jsonStatus.equals("True")) {


                                    //startActivity(new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class));
                                    //Toast.makeText(PrescriptionActivity.this, "Dosage", Toast.LENGTH_SHORT).show();

                                    Log.d("response_prescription", jsonMessage);
                                    if (check_response_size[0] < dosageLists.size()) {
                                        check_response_size[0]++;
                                    } else {
                                        EndSessionOnline();
                                    }


                                } else {

                                    // progressDialog.dismiss();
                                    final Dialog dialog = new Dialog(PrescriptionActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.text_dialog_ok);

                                    TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                    text.setText("Server not responding");

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
                                Toast.makeText(PrescriptionActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(PrescriptionActivity.this, R.style.DialogTheme)
                                    .setTitle("Warning!")
                                    .setMessage("Volley Error\n" + error.toString())
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
                    String auth = "Bearer " + new SessionManager(PrescriptionActivity.this).getToken();
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

    private void EndSessionOnline() throws JSONException {

        JSONObject orderJsonObject = new JSONObject();
        orderJsonObject.put("session_id", session_id);

        final String requestBody = orderJsonObject.toString();
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, new GlobalUrlApi().getNewBaseUrl() +
                "endVideoSession" ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response_prescription", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            //   JSONObject jsonData = jsonResponse.getJSONObject("Data");
                            // String SessionID = jsonResponse.getString("SessionID");
                            String jsonStatus = jsonResponse.getString("Status");
                            String jsonMessage = jsonResponse.getString("Message");

                            if (jsonStatus.equals("True")) {

                                Toast.makeText(PrescriptionActivity.this, "Medicines Prescribed to the patient", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                                // progressDialog.dismiss();
                                final Dialog dialog = new Dialog(PrescriptionActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Session Ended Medicines Prescribed");

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

                                // progressDialog.dismiss();
                                final Dialog dialog = new Dialog(PrescriptionActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Server not responding");

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
                            Toast.makeText(PrescriptionActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(PrescriptionActivity.this, R.style.DialogTheme)
                                .setTitle("Warning!")
                                .setMessage("Volley Error\n" + error.toString())
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
                String auth = "Bearer " + new SessionManager(PrescriptionActivity.this).getToken();
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

    public interface VolleyCallback {
        void onSuccess(ArrayList<PrescriptionIDList> prescriptionIDLists);
    }

}