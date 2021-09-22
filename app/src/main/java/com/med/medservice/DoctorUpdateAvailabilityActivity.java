package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.Models.DoctorAppointments.DocAppointmentAdapter;
import com.med.medservice.Models.PatientAppointments.AppointmentList;
import com.med.medservice.NetworkAPI.ApiCallerNew;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorUpdateAvailabilityActivity extends AppCompatActivity {

    String clickedDate;
    TextView selectedDate;


    LinearLayout appointmentLayout ;
    LinearLayout editLayout ;
    TextView appointmentButton ;
    TextView editButton ;


    String user_id;
    SessionManager sessionManager;


    RecyclerView appointmentRecycler;
    ArrayList<AppointmentList> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_update_availability);


        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        user_id = user.get(sessionManager.ID);

        Intent intent = getIntent();

        clickedDate = intent.getStringExtra("date");
        selectedDate = findViewById(R.id.selectedDate);
        selectedDate.setText(clickedDate);


        SetUI();

        GetAppointments();

    }

    private void GetAppointments() {

        appointmentRecycler = null;
        appointmentList = null;

        appointmentRecycler = findViewById(R.id.appoitmentsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        appointmentRecycler.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<AppointmentList>();

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_doctor_appointments_date.php?" +
                "doctor_id="+user_id+
                "&datee="+clickedDate,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            try {

                              //  Log.d("ApiResponse", response);

                                try {

                                    JSONArray parent = new JSONArray(response);

                                    for (int i = 0; i < parent.length(); i++) {
                                        JSONObject child = parent.getJSONObject(i);
                                        String id = child.getString("id");
                                        String patient_id = child.getString("patient_id");
                                        String doctor_id = child.getString("doctor_id");
                                        String patient_name = child.getString("patient_name");
                                        String doctor_name = child.getString("doctor_name");
                                        String email = child.getString("email");
                                        String phone = child.getString("phone");
                                        String date = child.getString("date");
                                        String time = child.getString("time");
                                        String problem = child.getString("problem");
                                        String status = child.getString("status");
                                        String day = child.getString("day");

                                        appointmentList.add(new AppointmentList(id, patient_id, doctor_id, patient_name, doctor_name, email, phone,
                                                date, time, problem, status,day));


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }



                        DocAppointmentAdapter adapter = new DocAppointmentAdapter(appointmentList, DoctorUpdateAvailabilityActivity.this);
                        appointmentRecycler.setAdapter(adapter);



                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private void SetUI() {

         appointmentLayout = findViewById(R.id.appointmentLayout);
         editLayout = findViewById(R.id.editLayout);


        appointmentButton = findViewById(R.id.appointmentButton);

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editButton.setBackgroundColor(Color.parseColor("#2c97ff"));
                appointmentButton.setBackgroundColor(Color.parseColor("#1A3C3C3C"));

                editButton.setTextColor(Color.parseColor("#FFFFFF"));
                appointmentButton.setTextColor(Color.parseColor("#232323"));

                editLayout.setVisibility(View.VISIBLE);
                appointmentLayout.setVisibility(View.GONE);

            }
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editButton.setBackgroundColor(Color.parseColor("#1A3C3C3C"));
                appointmentButton.setBackgroundColor(Color.parseColor("#2c97ff"));

                editButton.setTextColor(Color.parseColor("#232323"));
                appointmentButton.setTextColor(Color.parseColor("#FFFFFF"));

                editLayout.setVisibility(View.GONE);
                appointmentLayout.setVisibility(View.VISIBLE);

            }
        });
    }

    public void finish(View view) {
        finish();
    }
}