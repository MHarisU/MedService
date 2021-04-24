package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.med.medservice.Models.SessionsPatient.SessionsAdapter;
import com.med.medservice.Models.SessionsPatient.SessionsList;
import com.med.medservice.Utils.ApiTokenCaller;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SessionActivity extends AppCompatActivity {


    LinearLayout firstDec, secondOct;
    boolean firstDec_check = false;
    boolean secondOct_check = false;


    ArrayList<SessionsList> sessionsLists;
    RecyclerView sessionsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_session);

        firstDec = findViewById(R.id.firstDec);
        secondOct = findViewById(R.id.secondOct);


        getSessions();



    }

    private void getSessions() {
        sessionsRecycler = findViewById(R.id.sessionRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        sessionsRecycler.setLayoutManager(new LinearLayoutManager(this));
        sessionsLists = new ArrayList<SessionsList>();

        new ApiTokenCaller(SessionActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getPatientSessions?patient_id="+ new SessionManager(SessionActivity.this).getUserId(),
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {
                        Log.d("token_api_response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");

                            JSONArray arrayData = jsonResponse.getJSONArray("Data");


                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject child = arrayData.getJSONObject(i);


                                String session_id = child.getString("session_id");
                                String start_time = child.getString("start_time");
                                String end_time = child.getString("end_time");
                                String provider_notes = child.getString("provider_notes");

                                String created_at = child.getString("created_at");
                                String session_status = child.getString("session_status");
                                String diagnosis = child.getString("diagnosis");
                                String symptom_id = child.getString("symptom_id");

                                String doctor_first_name = child.getString("doctor_first_name");
                                String doctor_last_name = child.getString("doctor_last_name");
                                String symptoms_headache = child.getString("symptoms_headache");
                                String symptoms_fever = child.getString("symptoms_fever");
                                String symptoms_flu = child.getString("symptoms_flu");
                                String symptoms_nausea = child.getString("symptoms_nausea");
                                String symptoms_others = child.getString("symptoms_others");
                                String symptoms_description = child.getString("symptoms_description");


                                sessionsLists.add(new SessionsList(session_id, start_time, end_time, provider_notes, created_at, session_status, diagnosis,
                                        symptom_id, doctor_first_name, doctor_last_name, symptoms_headache, symptoms_fever, symptoms_flu, symptoms_nausea, symptoms_others, symptoms_description));


                            }


                            CardView cardProgress = findViewById(R.id.cardProgress);
                            cardProgress.setVisibility(View.GONE);

                            SessionsAdapter adapter = new SessionsAdapter(sessionsLists, SessionActivity.this);
                            sessionsRecycler.setAdapter(adapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );
    }


    public void Close(View view) {
        finish();
    }

    public void FirstDec(View view) {

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);



        if (!firstDec_check){
            firstDec.setVisibility(View.VISIBLE);
            firstDec.startAnimation(slide_down);
            firstDec_check = true;
        }
        else {
            firstDec.startAnimation(slide_up);

            firstDec.setVisibility(View.GONE);
            firstDec_check = false;
        }
    }

    public void SecondOct(View view) {


        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);



        if (!secondOct_check){
            secondOct.setVisibility(View.VISIBLE);
            secondOct.startAnimation(slide_down);
            secondOct_check = true;
        }
        else {
            secondOct.startAnimation(slide_up);

            secondOct.setVisibility(View.GONE);
            secondOct_check = false;
        }
    }

    public void OpenSessionDetail(View view) {
        startActivity(new Intent(getApplicationContext(), SessionDetailActivity.class));
    }
}