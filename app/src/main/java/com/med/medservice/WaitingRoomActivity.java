package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.FirebaseUserModel;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class WaitingRoomActivity extends AppCompatActivity {


    String name, user_id, role, user_email;
    SessionManager sessionManager;


    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String uniqueId;

    SwitchCompat switchButton;
    TextView doctorOnlineView;

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

        Query checkUser = reference.child("calling").child(user_id).child("pat_name");
        checkUser.addValueEventListener(new ValueEventListener() {
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


        SetupSwitchButton();

        GetDoctorStatus();


    }

    private void GetDoctorStatus() {


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
                    UpdateStatus("online");

                } else {
                    doctorOnlineView.setText("Offline");
                    doctorOnlineView.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_bg));
                    UpdateStatus("offline");

                }

            }
        });

    }

    public void UpdateStatus(String status) {




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


    public void Close(View view) {
        finish();
    }



    public void StartVideo(View view) {
        reference.child("calling").child(user_id).child("connID").setValue(uniqueId);
        reference.child("calling").child(user_id).child("isAvailable").setValue(true);
        startActivity(new Intent(WaitingRoomActivity.this, DoctorVideoActivity.class));
        finish();
    }
}