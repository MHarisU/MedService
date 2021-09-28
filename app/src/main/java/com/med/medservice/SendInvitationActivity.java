package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.Models.Category.CategorySquareAdapter;
import com.med.medservice.Models.OnlineDoctors.OnlineDoctorsList;
import com.med.medservice.NetworkAPI.ApiCallerNew;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.Utils.FirebaseUserModel;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SendInvitationActivity extends AppCompatActivity {


    String name, user_id, role, user_email;
    SessionManager sessionManager;


    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String uniqueId;

    OnlineDoctorsList currentData;
    TextView doctorName;

    boolean checkCallStarted = false;


    String symptoms_id, desc, session_id;
    String patient_link;
    String doctor_link;
    String room_id;
    Boolean sendInvitationCheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_send_invitation);


        uniqueId = UUID.randomUUID().toString();
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        role = user.get(sessionManager.USER_TYPE);
        user_id = user.get(sessionManager.ID);
        user_email = user.get(sessionManager.EMAIL);

        FirebaseUserModel userModel = new FirebaseUserModel(user_id, name, user_email, android_id);
        reference.child(user_id).setValue(userModel);


        Intent intent = getIntent();
        currentData = (OnlineDoctorsList) intent.getSerializableExtra("selectedDoctor");
        symptoms_id = intent.getStringExtra("symptoms_id");
        session_id = intent.getStringExtra("session_id");
        desc = intent.getStringExtra("desc");

        doctorName = findViewById(R.id.doctorName);
        doctorName.setText(currentData.getDoctor_name());


    }


    public void SendInvitation(View view) {


        String room_id = createRandomRoomId();
        Log.d("SendInvitationActivity", room_id);

        updateLinksOnline(room_id);





    }

    private String createRandomRoomId() {

        int randNum = getRandNum();
        String randString = getRandString();


        return (user_id + "_" + randNum + "_" + randString + "_" + (currentData.getDoctor_id()));
    }

    private String getRandString() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        // System.out.println(generatedString);

        return generatedString;
    }

    private int getRandNum() {

        Random rand = new Random(); //instance of random class
        int upperbound = 100000;
        //generate random values from 0-24
        int int_random = rand.nextInt(upperbound);

        return int_random;
    }

    private void updateLinksOnline(String room_id) {


   //     { "room_id": 5, "short_agent_url": 5, "short_visitor_url": 5, "symptoms_id": 5, "session_id": 13 }


        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("patient_link", "https://www.suunnoo.com/pages/r.html?room="+room_id+"&p=eyJsc1JlcFVybCI6Imh0dHBzOi8vd3d3LnN1dW5ub28uY29tLyIsImRpc2FibGVWaWRlbyI6MCwiZGlzYWJsZUF1ZGlvIjowLCJkaXNhYmxlU2NyZWVuU2hhcmUiOjEsImRpc2FibGVXaGl0ZWJvYXJkIjowLCJkaXNhYmxlVHJhbnNmZXIiOjEsImF1dG9BY2NlcHRWaWRlbyI6MSwiYXV0b0FjY2VwdEF1ZGlvIjoxfQ");
            orderJsonObject.put("patient_id", new SessionManager(SendInvitationActivity.this).getUserId());
            orderJsonObject.put("doctor_id", currentData.getDoctor_id());
            orderJsonObject.put("doctor_link", "https://www.suunnoo.com/pages/r.html?room="+room_id+"&p=eyJsc1JlcFVybCI6Imh0dHBzOi8vd3d3LnN1dW5ub28uY29tLyIsImRpc2FibGVWaWRlbyI6MCwiZGlzYWJsZUF1ZGlvIjowLCJkaXNhYmxlU2NyZWVuU2hhcmUiOjEsImRpc2FibGVXaGl0ZWJvYXJkIjowLCJkaXNhYmxlVHJhbnNmZXIiOjEsImF1dG9BY2NlcHRWaWRlbyI6MSwiYXV0b0FjY2VwdEF1ZGlvIjoxLCJpc0FkbWluIjoxfQ&isAdmin=1");
            orderJsonObject.put("room_id", room_id);
            orderJsonObject.put("short_agent_url", "3libvn");
            orderJsonObject.put("short_visitor_url", "q6rowi");
            orderJsonObject.put("symptom_id", symptoms_id);
            orderJsonObject.put("session_id", session_id);


            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = orderJsonObject.toString();
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, new GlobalUrlApi().getNewBaseUrl() +
                "createSentInvitation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("order_api_response", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            //   JSONObject jsonData = jsonResponse.getJSONObject("Data");
                           // String SessionID = jsonResponse.getString("SessionID");
                            String jsonStatus = jsonResponse.getString("Status");

                            if (jsonStatus.equals("True")) {

                                Toast.makeText(SendInvitationActivity.this, "Invitation Sent", Toast.LENGTH_SHORT).show();

                                getVideoUrlOnline();

                              /*  Toast.makeText(CreditCardPaymentActivity.this, ""+SessionID, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class);
                                intent.putExtra("session_id", SessionID);
                                intent.putExtra("symptoms_id", symptoms_id);
                                intent.putExtra("desc", desc);
                                intent.putExtra("selectedDoctor", currentData);
                                startActivity(intent);

                                //startActivity(new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class));
                                finish();*/


                            } else {

                                // progressDialog.dismiss();
                                final Dialog dialog = new Dialog(SendInvitationActivity.this);
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
                            Toast.makeText(SendInvitationActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SendInvitationActivity.this, R.style.DialogTheme)
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
                String auth = "Bearer " + new SessionManager(SendInvitationActivity.this).getToken();
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

    private void getVideoUrlOnline() {

        new ApiTokenCaller(SendInvitationActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getVideoLinks?session_id="+session_id,
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {
                        //Log.d("token_api_response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            Log.d("api_response", jsonResponse.toString());


                            JSONArray arrayData = jsonResponse.getJSONArray("Data");


                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject child = arrayData.getJSONObject(i);
                                String id = child.getString("id");
                                patient_link = child.getString("patient_link");
                                doctor_link = child.getString("doctor_link");
                                room_id = child.getString("room_id");
                               // String thumbnail = child.getString("thumbnail");



                            }


                            sendFirebaseInvitation();

                            //categoryList.add(new CategoryList("0", "Other", "lab-test", "none", "thumbnail"));


                            //CategorySquareAdapter adapter = new CategorySquareAdapter(categoryList, getApplicationContext());
                            //categoryRecycler.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        );

    }

    private void sendFirebaseInvitation() {

        if (!sendInvitationCheck){

            reference.child("calling").child(currentData.getDoctor_id()).child("incoming").setValue(user_id);
            reference.child("calling").child(currentData.getDoctor_id()).child("pat_name").setValue(name);
            reference.child("calling").child(currentData.getDoctor_id()).child("session_id").setValue(session_id);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    reference.child("calling").child(currentData.getDoctor_id()).child("isAvailable").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            try {
                                if (snapshot.getValue().toString() == "true") {
                                    reference.child("calling").child(currentData.getDoctor_id()).child("connID").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            //  Toast.makeText(SendInvitationActivity.this, "" + snapshot.getValue().toString() + " accepted call", Toast.LENGTH_SHORT).show();

                                            if (!checkCallStarted) {
                                                checkCallStarted = true;
                                                Intent intent = new Intent(SendInvitationActivity.this, PatientVideoActivity.class);
                                                intent.putExtra("session_id", session_id);
                                                intent.putExtra("patient_link", patient_link);
                                                intent.putExtra("room_id", room_id);
                                                intent.putExtra("selectedDoctor", currentData);
                                                startActivity(intent);
                                                //startActivity(new Intent(SendInvitationActivity.this, PatientVideoActivity.class));
                                                finish();
                                            }
                                            //Toast.makeText(SendInvitationActivity.this, "" + snapshot.getValue().toString() + " accepted call\n"+uniqueId, Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
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
            }, 2000);

            TextView sendInvitationText = findViewById(R.id.sendInvitationText);
            TextView indicatorView = findViewById(R.id.indicatorView);

            indicatorView.setText("Invitation sent please wait for doctor to start the session");

            sendInvitationText.setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg_invert));
            sendInvitationText.setText("INVITATION SENT");

            CreateSession(user_id, currentData.getDoctor_id(), desc, symptoms_id);

            sendInvitationCheck = true;
        }


    }


    private void CreateSession(String user_id, String doctor_id, String description, String symptom_id) {


        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "create_session.php?" +
                "patient_id=" + user_id +
                "&doctor_id=" + doctor_id +
                "&notes=" + description +
                "&symp_id=" + symptom_id,
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
                                    String session_id = child.getString("id");
                                    //Toast.makeText(SendInvitationActivity.this, ""+session_id, Toast.LENGTH_LONG).show();


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

    public void StartVideo(View view) {
        // reference.child("calling").child(currentData.getDoctor_id()).setValue(null);

        // startActivity(new Intent(SendInvitationActivity.this, PatientVideoActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    protected void onDestroy() {
        // reference.child("calling").child(currentData.getDoctor_id()).setValue(null);
        super.onDestroy();
    }
}