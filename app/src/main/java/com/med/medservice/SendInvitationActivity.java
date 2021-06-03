package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.med.medservice.Models.OnlineDoctors.OnlineDoctorsList;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.FirebaseUserModel;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
    Boolean sendInvitationCheck =false;



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

        if (!sendInvitationCheck){

            reference.child("calling").child(currentData.getDoctor_id()).child("incoming").setValue(user_id);
            reference.child("calling").child(currentData.getDoctor_id()).child("pat_name").setValue(name);
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