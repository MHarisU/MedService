package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.med.medservice.Models.OnlineDoctors.OnlineDoctorsList;
import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.SymptomSelectorDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EvisitFormActivity extends AppCompatActivity implements SymptomSelectorDialogFragment.onMultiChoiceListener {

    TextView symtoms_text;

    OnlineDoctorsList currentData;
    String headache = "0";
    String fever = "0";
    String flu = "0";
    String nausea = "0";
    String other = "0";

    TextView patDescription;

    String name, user_id, role, user_email;
    SessionManager sessionManager;

    String symptoms_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_evisit_form);

        GetUserFromManager();

        patDescription = findViewById(R.id.patDescription);

        Intent intent = getIntent();
        currentData = (OnlineDoctorsList) intent.getSerializableExtra("selectedDoctor");
        //Toast.makeText(this, ""+currentData.getDoctor_name(), Toast.LENGTH_SHORT).show();
        TextView DoctorNameEvisit = findViewById(R.id.DoctorNameEvisit);
        DoctorNameEvisit.setText("E-Visit with " + currentData.getDoctor_name());
        symtoms_text = findViewById(R.id.symtoms_text);

    }

    private void GetUserFromManager() {

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        role = user.get(sessionManager.USER_TYPE);
        user_id = user.get(sessionManager.ID);
        user_email = user.get(sessionManager.EMAIL);

    }

    public void Close(View view) {
        finish();
    }

    public void SelectSymptoms(View view) {
        DialogFragment symptomsDialog = new SymptomSelectorDialogFragment();
        symptomsDialog.setCancelable(false);
        symptomsDialog.show(getSupportFragmentManager(), "Multichoice Dialog");
    }


    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedSymptoms) {
        headache = "0";
        fever = "0";
        flu = "0";
        nausea = "0";
        other = "0";


        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilders = new StringBuilder();
        stringBuilder.append("Selected Symptoms:");
        for (String str : selectedSymptoms) {
            stringBuilder.append("\n- " + str);
            stringBuilders.append(str + ",");

            if (str.equals("Headache")) {
                headache = "1";
            }
            if (str.equals("Fever")) {
                fever = "1";
            }
            if (str.equals("Flu")) {
                flu = "1";
            }
            if (str.equals("Nausea")) {
                nausea = "1";
            }
            if (str.equals("Other")) {
                other = "1";
            }

        }
        //SelectedSymp = stringBuilders.toString();
        symtoms_text.setText(stringBuilder);

    }

    @Override
    public void onNegativeButtonClicked() {

        symtoms_text.setText("Select Symptoms");
    }

    public void Submit(View view) {

        if (symtoms_text.getText().toString() != null && !symtoms_text.getText().toString().equals("")) {

            TextView submitButton = findViewById(R.id.submitButton);
            submitButton.setVisibility(View.GONE);

            String desc = patDescription.getText().toString();

            CreateSymptoms(desc);


        } else {
            Toast.makeText(this, "Please select symptoms", Toast.LENGTH_SHORT).show();
        }

    }

    private void CreateSymptoms(final String desc) {

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "create_symptoms.php?" +
                "patient_id=" + user_id +
                "&doctor_id=" + currentData.getDoctor_id() +
                "&headache=" + headache +
                "&fever=" + fever +
                "&flu=" + flu +
                "&nausea=" + nausea +
                "&others=" + other +
                "&description=" + desc
                ,
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
                                    symptoms_id = child.getString("id");
                                    //Toast.makeText(EvisitFormActivity.this, "" + symptoms_id, Toast.LENGTH_LONG).show();


                                    Intent intent = new Intent(EvisitFormActivity.this, CreditCardPaymentActivity.class);
                                    intent.putExtra("symptoms_id", symptoms_id);
                                    intent.putExtra("desc", desc);
                                    intent.putExtra("selectedDoctor", currentData);
                                    startActivity(intent);

                                    //startActivity(new Intent(EvisitFormActivity.this, CreditCardPaymentActivity.class));
                                    finish();
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
}