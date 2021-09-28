package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
import java.util.UUID;

public class WaitingRoomActivity extends AppCompatActivity {


    String name, user_id, role, user_email;
    SessionManager sessionManager;


    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String uniqueId;

    SwitchCompat switchButton;
    TextView doctorOnlineView;

    String symptoms_id, desc, session_id, pat_id, doctor_link, room_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_waiting_room);

        uniqueId = UUID.randomUUID().toString();
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        //    reference.setValue(uniqueId);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        role = user.get(sessionManager.USER_TYPE);
        user_id = user.get(sessionManager.ID);
        user_email = user.get(sessionManager.EMAIL);


        FirebaseUserModel userModel = new FirebaseUserModel(user_id, name, user_email, android_id);
        reference.child(user_id).setValue(userModel);

        Query checkUser = reference.child("calling").child(user_id).child("incoming");
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {


                    pat_id = snapshot.getValue().toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Query checkUserName = reference.child("calling").child(user_id).child("pat_name");
        checkUserName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    TextView patNameView = findViewById(R.id.patNameView);
                    CardView onlinePatCard = findViewById(R.id.onlinePatCard);

                    //Toast.makeText(WaitingRoomActivity.this, ""+snapshot.getValue().toString()+" is calling", Toast.LENGTH_SHORT).show();

                    patNameView.setText(snapshot.getValue().toString());
                    onlinePatCard.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query session_node = reference.child("calling").child(user_id).child("session_id");
        session_node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    //TextView patNameView = findViewById(R.id.patNameView);
                   // CardView onlinePatCard = findViewById(R.id.onlinePatCard);

                    Toast.makeText(WaitingRoomActivity.this, ""+snapshot.getValue().toString()+" Session ID", Toast.LENGTH_SHORT).show();

                    session_id = snapshot.getValue().toString();
                    getDoctorLink();
                   // patNameView.setText(snapshot.getValue().toString());
                   // onlinePatCard.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        SetupSwitchButton();

        GetDoctorStatus();


    }

    private void getDoctorLink() {
        new ApiTokenCaller(WaitingRoomActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getVideoLinks?session_id="+session_id,
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
                                //patient_link = child.getString("patient_link");
                                doctor_link = child.getString("doctor_link");
                                room_id = child.getString("room_id");
                                // String thumbnail = child.getString("thumbnail");



                            }


                            //sendFirebaseInvitation();

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

    private void GetDoctorStatus() {

        //old api
        /*

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_doctor_status.php?" +
                "doctor_id=" + user_id ,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            //  Log.d("ApiResponse", response);

                            try {

                                JSONArray parent = new JSONArray(response);

                                JSONObject object = parent.getJSONObject(0);

                                if (object.getString("status").equals("online")){
                                    switchButton.setChecked(true);
                                }else {
                                    switchButton.setChecked(false);
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
*/


        new ApiTokenCaller(WaitingRoomActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getDoctorStatus?" +
                "doctor_id=" +user_id,
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {
                        Log.d("token_api_response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");

                            String arrayData = jsonResponse.getString("Data");



                            if (arrayData.equals("Online")){
                                switchButton.setChecked(true);
                            }else if (arrayData.equals("Offline")){
                                switchButton.setChecked(false);
                            }
                            Toast.makeText(WaitingRoomActivity.this, ""+arrayData, Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );

    }

    private void SetupSwitchButton() {
        doctorOnlineView = findViewById(R.id.doctorOnlineView);
        switchButton = findViewById(R.id.switchButton);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    doctorOnlineView.setText("Online");
                    doctorOnlineView.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bg));
                    //UpdateStatus("online");
                    UpdateStatus("1");

                } else {
                    doctorOnlineView.setText("Offline");
                    doctorOnlineView.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_bg));
                   // UpdateStatus("offline");
                    UpdateStatus("0");

                }

            }
        });

    }

    public void UpdateStatu(String status) {




        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "update_doctor_online_status.php?" +
                "doctor_id=" + user_id +
                "&status=" + status,
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


                        try {

                        }catch (NullPointerException e){

                            if (response.equals("1")) {

                                // PatientProfileActivity.getInstance().UpdateSuccessful();
                            } else {

                                final Dialog dialog = new Dialog(WaitingRoomActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Error! Status did not changed properly");

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



                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private void UpdateStatus(String status) {

        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("active", status);
            if (status.equals("1")) {
                orderJsonObject.put("status", "online");
            }
            else
                orderJsonObject.put("status", "offline");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = orderJsonObject.toString();
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();

        Log.d("order_api_response", new GlobalUrlApi().getNewBaseUrl() + "updateDoctorStatus/"+user_id);



        StringRequest stringRequest = new StringRequest(Request.Method.PUT, new GlobalUrlApi().getNewBaseUrl() + "updateDoctorStatus/"+user_id,
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


                                Toast.makeText(WaitingRoomActivity.this, "Status Updated", Toast.LENGTH_SHORT).show();


                            } 


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //   login_button.setVisibility(View.VISIBLE);
                            //   progress_bar.setVisibility(View.GONE);
                            // Toast.makeText(LoginActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(WaitingRoomActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(WaitingRoomActivity.this, R.style.DialogTheme)
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
                String auth = "Bearer " + new SessionManager(WaitingRoomActivity.this).getToken();
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



    public void Close(View view) {
        finish();
    }



    public void StartVideo(View view) {
        reference.child("calling").child(user_id).child("connID").setValue(uniqueId);
        reference.child("calling").child(user_id).child("isAvailable").setValue(true);
        Intent intent = new Intent(WaitingRoomActivity.this, DoctorVideoActivity.class);
        intent.putExtra("session_id", session_id);
        intent.putExtra("doctor_link", doctor_link);
        intent.putExtra("room_id", room_id);
        intent.putExtra("pat_id", pat_id);
        startActivity(intent);
        finish();
    }
}