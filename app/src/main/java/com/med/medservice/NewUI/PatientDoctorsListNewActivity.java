package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.med.medservice.Models.PatientDoctors.PatientDoctorsAdapter;
import com.med.medservice.Models.PatientDoctors.PatientDoctorsList;
import com.med.medservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientDoctorsListNewActivity extends AppCompatActivity {

    ArrayList<PatientDoctorsList> DoctorsList;
    RecyclerView doctorsRecyclerView;
    private static final String URL = "https://demo.umbrellamd-video.com/api/mydoctor";
    private String imageURL;

    Dialog docBioDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_doctors_list_new);

//        setUpBioDialog();

        setUpDoctorsListRecyclerView();

    }

    private void setUpDoctorsListRecyclerView() {

        doctorsRecyclerView = findViewById(R.id.doctorListRecycler);
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(PatientDoctorsListNewActivity.this));
        DoctorsList = new ArrayList<PatientDoctorsList>();
        extractDoctorsList();


    }

    private void extractDoctorsList() {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest doctorsListData = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String success = response.getString("success");
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    String firstName = dataObject.getString("name");
                    String lastName = dataObject.getString("last_name");
                    String specialization = dataObject.getString("specialization");
                    String docImage = dataObject.getString("user_image");
                    String rating = dataObject.getString("rating");

                        DoctorsList.add(new PatientDoctorsList(firstName, lastName, specialization));

                    }
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }


                PatientDoctorsAdapter doctorListAdapter;
                doctorListAdapter = new PatientDoctorsAdapter(PatientDoctorsListNewActivity.this, DoctorsList);
                doctorsRecyclerView.setAdapter(doctorListAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse" + error.getMessage());

            }
        });


        queue.add(doctorsListData);

    }



//    private void setUpBioDialog() {
//        docBioDialog = new Dialog(PatientDoctorsListNewActivity.this);
//        docBioDialog.setContentView(R.layout.doctor_bio_dialog);
//        docBioDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        docBioDialog.setCancelable(false);
//
//        Button docBioButton = findViewById(R.id.viewDocBio);
//        docBioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                docBioDialog.show();
//
//            }
//        });
//
//
//    }

    public void openBookingAppointmentActivity(View view) {
        startActivity(new Intent(PatientDoctorsListNewActivity.this, PatientBookingAppoitmentDetailsNewActivity.class));
    }

    public void GoBackToMain(View view) {
        finish();    }


    public void closeBio(View view) {
        docBioDialog.dismiss();
    }
}