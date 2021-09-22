package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.med.medservice.Models.SessionItems.SessionsItemsAdapter;
import com.med.medservice.Models.SessionItems.SessionsItemsList;
import com.med.medservice.Models.SessionsPatient.SessionsList;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientPrescriptionWaitingActivity extends AppCompatActivity {

    String symptoms_id, desc, session_id;
    // OnlineDoctorsList currentData;

    LinearLayout prescriptionLayout, loadingLayout;


    SessionsList currentData;

    TextView doctorName, diagnosisView, notesView, sessionDate, symptomsView, descriptionView;

    // RecyclerView sessionsRecycler;


    ArrayList<SessionsItemsList> itemsList;
    RecyclerView itemsRecycler;


    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_prescription_waiting);

        Intent intent = getIntent();
        //currentData = (OnlineDoctorsList) intent.getSerializableExtra("selectedDoctor");
        session_id = intent.getStringExtra("session_id");

        setupUI();


        //EndSessionFunc();

        CheckSessionEnded();


    }

    private void CheckSessionEnded() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        Query checkUser = reference.child("sessionEnded").child(new SessionManager(PatientPrescriptionWaitingActivity.this).getUserId()).child("session_id");
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {

                    if (snapshot.getValue().toString() != null) {
                        Toast.makeText(PatientPrescriptionWaitingActivity.this, "" + snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();

                        if (session_id.equals(snapshot.getValue().toString())) {
                            getSessionDetails();

                            getPrescribedMeds();
                        }
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

    private void EndSessionFunc() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.child("sessionEnded").child("2").child("doc_id").setValue("1");

    }

    private void getPrescribedMeds() {
        itemsRecycler = findViewById(R.id.itemsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemsRecycler.setLayoutManager(new LinearLayoutManager(this));
        itemsList = new ArrayList<SessionsItemsList>();

        new ApiTokenCaller(PatientPrescriptionWaitingActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getPrescribedMedicines?session_id=" + session_id,
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
                                String product_id = child.getString("product_id");
                                String product_name = child.getString("product_name");
                                String product_mode = child.getString("product_mode");

                                String prescription_comment = child.getString("prescription_comment");
                                String usage = child.getString("usage");
                                String quantity = child.getString("quantity");
                                String product_status = child.getString("product_status");


                                itemsList.add(new SessionsItemsList(session_id, product_id, product_name, product_mode, prescription_comment, usage, quantity, product_status));


                            }

                            /*
                            CardView cardProgress = findViewById(R.id.cardProgress);
                            cardProgress.setVisibility(View.GONE);*/

                            SessionsItemsAdapter adapter = new SessionsItemsAdapter(itemsList, PatientPrescriptionWaitingActivity.this);
                            itemsRecycler.setAdapter(adapter);

                            fetchItemsForCart();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        );
    }

    private void fetchItemsForCart() {

        CartDBHelper mydb;
        mydb = new CartDBHelper(this);
        mydb.removeAllItems(0);


        for (SessionsItemsList item: itemsList){

            new ApiTokenCaller(PatientPrescriptionWaitingActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProducts?id="+(item.getProduct_id()),
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


                                    String id = child.getString("id");
                                    String name = child.getString("name");
                                    String featured_image = child.getString("featured_image");
                                    String sale_price = child.getString("sale_price");
                                    String regular_price = child.getString("regular_price");
                                    String mode = child.getString("mode");

                                  //  popularMedsList.add(new MedicineList(id, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                  //          quantity, short_description, description, stock_status));


                                    String user_id = new SessionManager(PatientPrescriptionWaitingActivity.this).getUserId();

                                    if (sale_price != null && !sale_price.equals("null") && !sale_price.equals("")) {
                                        mydb.insertCartItem(user_id, id, name, item.getQuantity(), sale_price, "0", mode, featured_image);

                                    } else
                                        mydb.insertCartItem(user_id, id, name, item.getQuantity(), regular_price, "0", mode, featured_image);

                                    CardView gotoCart = findViewById(R.id.gotoCart);
                                    gotoCart.setVisibility(View.VISIBLE);

                                }





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }
            );

        }


    }

    private void getSessionDetails() {

        // sessionsRecycler = findViewById(R.id.sessionRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  sessionsRecycler.setLayoutManager(new LinearLayoutManager(this));
        //currentData = new ArrayList<SessionsList>();

        new ApiTokenCaller(PatientPrescriptionWaitingActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getPatientSessions?patient_id=" + new SessionManager(PatientPrescriptionWaitingActivity.this).getUserId(),
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");

                            JSONArray arrayData = jsonResponse.getJSONArray("Data");


                            for (int i = 0; i < 1; i++) {
                                JSONObject child = arrayData.getJSONObject(i);

                                Log.d("token_api_response", child.toString());


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


                                currentData = new SessionsList(session_id, start_time, end_time, provider_notes, created_at, session_status, diagnosis,
                                        symptom_id, doctor_first_name, doctor_last_name, symptoms_headache, symptoms_fever, symptoms_flu, symptoms_nausea, symptoms_others, symptoms_description);


                                fillSessionDetailIntoUI();


                            }


                            //   CardView cardProgress = findViewById(R.id.cardProgress);
                            //  cardProgress.setVisibility(View.GONE);

                            //  SessionsAdapter adapter = new SessionsAdapter(sessionsLists, PatientPrescriptionWaitingActivity.this);
                            // sessionsRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        );

    }

    private void fillSessionDetailIntoUI() {

        doctorName = findViewById(R.id.doctorName);
        diagnosisView = findViewById(R.id.diagnosisView);
        notesView = findViewById(R.id.notesView);
        sessionDate = findViewById(R.id.sessionDate);
        symptomsView = findViewById(R.id.symptomsView);
        descriptionView = findViewById(R.id.descriptionView);

        doctorName.setText("Dr." + currentData.getDoctor_first_name() + " " + currentData.getDoctor_last_name());
        String diag = (currentData.getDiagnosis().replace("<p>", "")).replace("</p>", "");
        if (diag.equals("null") || diag == null) {
            diag = "empty";
        }
        diagnosisView.setText(diag);
        String note = currentData.getProvider_notes();
        note = (note.replace("<p>", "")).replace("</p>", "");
        if (note != null && !note.equals("null") && !note.equals(""))
            notesView.setText(note);
        sessionDate.setText(currentData.getCreated_at());
        descriptionView.setText(currentData.getSymptoms_description());
        String symptoms = "";
        if (currentData.getSymptoms_headache().equals("1"))
            symptoms = symptoms + "Headache, ";

        if (currentData.getSymptoms_fever().equals("1"))
            symptoms = symptoms + "Fever, ";

        if (currentData.getSymptoms_flu().equals("1"))
            symptoms = symptoms + "Flu, ";

        if (currentData.getSymptoms_nausea().equals("1"))
            symptoms = symptoms + "Nausea, ";

        if (currentData.getSymptoms_others().equals("1"))
            symptoms = symptoms + "Others.";

        symptomsView.setText(symptoms);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingLayout.setVisibility(View.GONE);
                prescriptionLayout.setVisibility(View.VISIBLE);
            }
        }, 1000);

    }

    private void setupUI() {
        prescriptionLayout = findViewById(R.id.prescriptionLayout);
        loadingLayout = findViewById(R.id.loadingLayout);

    }

    public void Close(View view) {
        finish();
    }

    public void CartOpen(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }
}