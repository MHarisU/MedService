package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.med.medservice.Models.PatientAppointments.AppointmentAdapter;
import com.med.medservice.Models.PatientAppointments.AppointmentList;
import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Models.ProductMedicine.MedicineListAdapter;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientAppointmentsActivity extends AppCompatActivity {

    RecyclerView appointmentRecycler;
    ArrayList<AppointmentList> appointmentList;


    String user_id;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_appointments);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        user_id = user.get(sessionManager.ID);


       // GetAppoitments();
    }


    private void GetAppoitments() {

        appointmentRecycler = null;
        appointmentList = null;

        appointmentRecycler = findViewById(R.id.appoitmentsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        appointmentRecycler.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<AppointmentList>();

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_patient_appointments.php?patient_id="+user_id,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            try {

                                Log.d("ApiResponse", response);

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


                        AppointmentAdapter adapter = new AppointmentAdapter(appointmentList, PatientAppointmentsActivity.this);
                        appointmentRecycler.setAdapter(adapter);


                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_up);
                       // cardProgress.setVisibility(View.GONE);
                      //  layoutMain.setVisibility(View.VISIBLE);
                      //  layoutMain.startAnimation(slide_up);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    public void Close(View view) {
        finish();
    }

    public void OpenBookAppointment(View view) {
        startActivity(new Intent(getApplicationContext(), BookAppointmentActivity.class));
    }

    public void OpenHome(View view) {
        Intent i = new Intent(this, PatientMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetAppoitments();
    }
}