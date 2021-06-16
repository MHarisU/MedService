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
import android.widget.TextView;

import com.med.medservice.Models.SessionItems.SessionsItemsAdapter;
import com.med.medservice.Models.SessionItems.SessionsItemsList;
import com.med.medservice.Models.SessionsPatient.SessionsAdapter;
import com.med.medservice.Models.SessionsPatient.SessionsList;
import com.med.medservice.Utils.ApiTokenCaller;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SessionDetailActivity extends AppCompatActivity {

    SessionsList currentData;

    TextView doctorName, diagnosisView, notesView, sessionDate, symptomsView, descriptionView;


    ArrayList<SessionsItemsList> itemsList;
    RecyclerView itemsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_session_detail);

        getSession();

        initUI();

        getItems();
    }

    private void getItems() {
        itemsRecycler = findViewById(R.id.itemsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemsRecycler.setLayoutManager(new LinearLayoutManager(this));
        itemsList = new ArrayList<SessionsItemsList>();

        new ApiTokenCaller(SessionDetailActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getPrescribedMedicines?session_id=" +currentData.getSession_id(),
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

                            SessionsItemsAdapter adapter = new SessionsItemsAdapter(itemsList, SessionDetailActivity.this);
                            itemsRecycler.setAdapter(adapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );
    }


    private void initUI() {

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

    }

    private void getSession() {
        Intent intent = getIntent();
        currentData = (SessionsList) intent.getSerializableExtra("session");
    }

    public void Close(View view) {
        finish();
    }
}