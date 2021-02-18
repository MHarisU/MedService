package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.med.medservice.Utils.GlobalUrlApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DoctorRegisterActivity extends AppCompatActivity {

    EditText editText_first, editText_last, editText_email, editText_phone, editText_address, editText_nip, editText_upin, editText_password;
    String selected_date_of_birht = "";
    String selected_state = "";
    String selected_spec = "";

    ProgressBar progress_bar;
    Button patient_register_button;

    private static String URL_Register;
    GlobalUrlApi globalUrlApi;

    String[] stateNames;
    String[] specNames;
    Spinner statesSpinner, spinner_spec, monthSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_register);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        statesSpinner = findViewById(R.id.spinner_state);
        spinner_spec = findViewById(R.id.spinner_spec);

        SetupDoctorSpinner();
        SetupSpecileSpinner();


        editText_first = findViewById(R.id.editText_first);
        editText_last = findViewById(R.id.editText_last);
        editText_email = findViewById(R.id.editText_email);
        editText_phone = findViewById(R.id.editText_phone);
        editText_address = findViewById(R.id.editText_address);
        editText_nip = findViewById(R.id.editText_nip);
        editText_upin = findViewById(R.id.editText_upin);
        editText_password = findViewById(R.id.editText_password);
        progress_bar = findViewById(R.id.progress_bar);
        patient_register_button = findViewById(R.id.patient_register_button);

        globalUrlApi = new GlobalUrlApi();
        URL_Register = globalUrlApi.getUrlAppFolder() + "register_doc.php";
    }



    private void SetupSpecileSpinner() {
        specNames = new String[8];
        specNames[0] = "Select Your Specialization";
        specNames[1] = "Heart";
        specNames[2] = "Dental";
        specNames[3] = "Kidney";
        specNames[4] = "Eye";
        specNames[5] = "Lung";
        specNames[6] = "Psychiatrist";
        specNames[7] = "Skin Care";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, specNames);

        spinner_spec.setAdapter(adapter);

        spinner_spec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = spinner_spec.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                // if (positions != 0)
                //Toast.makeText(BookAppointmentActivity.this, "" + courseName[positions], Toast.LENGTH_SHORT).show();
                if (specNames[positions].equals("Select Your Specialization")) {
                //    Toast.makeText(DoctorRegisterActivity.this, "Select a Specialization please", Toast.LENGTH_SHORT).show();
                    selected_spec = "";
                } else {
                    selected_spec = specNames[positions];
              //      Toast.makeText(DoctorRegisterActivity.this, "" + specNames[positions], Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    private void SetupDoctorSpinner() {
        stateNames = new String[5];
        stateNames[0] = "Select Your State";
        stateNames[1] = "Alaska";
        stateNames[2] = "Arizona Arkansas";
        stateNames[3] = "California";
        stateNames[4] = "Colorado";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, stateNames);

        statesSpinner.setAdapter(adapter);

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = statesSpinner.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                // if (positions != 0)
                //Toast.makeText(BookAppointmentActivity.this, "" + courseName[positions], Toast.LENGTH_SHORT).show();
                if (stateNames[positions].equals("Select Your State")) {
                //    Toast.makeText(DoctorRegisterActivity.this, "Select a state please", Toast.LENGTH_SHORT).show();
                    selected_state = "";
                } else {
                    selected_state = stateNames[positions];
               //     Toast.makeText(DoctorRegisterActivity.this, "" + stateNames[positions], Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    public void Close(View view) {
        finish();
    }

    public void OpenLogin(View view) {
        finish();
    }

    public void RegisterButton(View view) {

     //   Toast.makeText(this, selected_state+"\n"+selected_spec, Toast.LENGTH_SHORT).show();



        progress_bar.setVisibility(View.VISIBLE);
        patient_register_button.setVisibility(View.GONE);

        final String first = editText_first.getText().toString();
        final String last = editText_last.getText().toString();
        final String email = editText_email.getText().toString();
        final String phone = editText_phone.getText().toString();
        final String address = editText_address.getText().toString();
        final String npi = editText_nip.getText().toString();
        final String upin = editText_upin.getText().toString();
        final String password = editText_password.getText().toString();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (
                        first != null && !first.equals("") &&
                                last != null && !last.equals("") &&
                                selected_date_of_birht != null && !selected_date_of_birht.equals("") &&
                                email != null && !email.equals("") &&
                                phone != null && !phone.equals("") &&
                                address != null && !address.equals("") &&
                                npi != null && !npi.equals("") &&
                                upin != null && !upin.equals("") &&
                                selected_state != null && !selected_state.equals("") &&
                                selected_spec != null && !selected_spec.equals("") &&
                                password != null && !password.equals("")) {


                    Register(first, last, selected_date_of_birht, email, phone, address, npi, upin, selected_state, selected_spec, password);
                    //Toast.makeText(DoctorRegisterActivity.this, "All done", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DoctorRegisterActivity.this, "Please fill details correctly", Toast.LENGTH_SHORT).show();

                    progress_bar.setVisibility(View.GONE);
                    patient_register_button.setVisibility(View.VISIBLE);
                }
            }
        }, 10);


    }

    private void Register(final String first, final String last, final String DOB, final String email, final String phone, final String address, final String npi, final String upin, final String state, final String spec, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //  JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {

                                patient_register_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorRegisterActivity.this, R.style.DialogTheme)
                                        .setTitle("info")
                                        .setMessage("Registration Complete Now Please Login")
                                        .setCancelable(false)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                patient_register_button.setVisibility(View.VISIBLE);
                                                progress_bar.setVisibility(View.GONE);
                                                finish();

                                            }
                                        });
                                //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                dialog.show();

                            } else if (success.equals("0")) {

                                patient_register_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorRegisterActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("Email already registered")
                                        .setCancelable(false)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                patient_register_button.setVisibility(View.VISIBLE);
                                                progress_bar.setVisibility(View.GONE);

                                            }
                                        });
                                //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                dialog.show();


                            } else if (success.equals("2")) {

                                patient_register_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorRegisterActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("Server is busy right now. ")
                                        .setCancelable(false)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                patient_register_button.setVisibility(View.VISIBLE);
                                                progress_bar.setVisibility(View.GONE);

                                            }
                                        });
                                //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                dialog.show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            patient_register_button.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);
                            // Toast.makeText(LoginActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(DoctorRegisterActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                            //  login_text.setVisibility(View.VISIBLE);
                            // login_text.setText("JSON Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  login_button.setVisibility(View.VISIBLE);
                        //  progress_bar.setVisibility(View.GONE);
                        //  Toast.makeText(LoginActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(DoctorRegisterActivity.this, "VolleyError", Toast.LENGTH_SHORT).show();
                        // Register(email, password);
                        //    login_text.setVisibility(View.VISIBLE);
                        //   login_text.setText("Error from php");

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_type", "doctor");
                params.put("first_name", first);
                params.put("last_name", last);
                params.put("email", email);
                params.put("password", password);
                params.put("date_of_birth", selected_date_of_birht);
                params.put("phone_number", phone);
                params.put("address", address);
                params.put("state", selected_state);
                params.put("npi", npi);
                params.put("upin", upin);
                params.put("spec", selected_spec);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0; //retry turn off
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void SelectDateTime(View view) {
        final TextView date_time = findViewById(R.id.date_time);

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                selected_date_of_birht = simpleDateFormat.format(calendar.getTime());

                date_time.setText("Date of Birth: " + simpleDateFormat.format(calendar.getTime()));


            }
        };

        new DatePickerDialog(DoctorRegisterActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


    }

}