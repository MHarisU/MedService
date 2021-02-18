package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.ViewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText nameView, nameLastView, emailView, phoneView, addressView;
    TextView dateView;


    String name;
    String last_name;
    String email;
    String date_of_birth;
    String phone_number;
    String office_address;


    String user_id;
    SessionManager sessionManager;

    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        // name = user.get(sessionManager.FIRST_NAME);
        //  name = name + " " + user.get(sessionManager.LAST_NAME);
        //  email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);

        Intent intent = getIntent();
        name = intent.getStringExtra("first_name");
        last_name = intent.getStringExtra("last_name");
        date_of_birth = intent.getStringExtra("date");
        email = intent.getStringExtra("email");
        phone_number = intent.getStringExtra("phone");
        office_address = intent.getStringExtra("address");


        loadUI();


    }

    private void loadUI() {
        nameView = findViewById(R.id.nameView);
        nameLastView = findViewById(R.id.nameLastView);
        emailView = findViewById(R.id.emailView);
        phoneView = findViewById(R.id.phoneView);
        addressView = findViewById(R.id.addressView);
        dateView = findViewById(R.id.dateView);

        nameView.setText(name);
        nameLastView.setText(last_name);
        emailView.setText(email);
        phoneView.setText(phone_number);
        addressView.setText(office_address);
        dateView.setText(date_of_birth);
    }

    public void Close(View view) {
        finish();
    }

    public void SelectDate(View view) {
        final TextView date_time = findViewById(R.id.dateView);

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                date_time.setText("" + simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(EditProfileActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    public void Update(View view) {
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "/update_user_info.php?" +
                "id=" +user_id+
                "&first_name=" +nameView.getText().toString()+
                "&last_name=" +nameLastView.getText().toString()+
                "&date=" +dateView.getText().toString()+
                "&phone=" +phoneView.getText().toString()+
                "&address=" + addressView.getText().toString(),
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

                       if (response.equals("1")){

                           finish();
                           PatientProfileActivity.getInstance().UpdateSuccessful();
                       }

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}