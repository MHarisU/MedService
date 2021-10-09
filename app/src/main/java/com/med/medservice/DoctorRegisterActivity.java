package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.med.medservice.Diaglogs.SearchZipStateCity;
import com.med.medservice.Diaglogs.ViewDialogRegistration;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DoctorRegisterActivity extends AppCompatActivity {

    EditText editText_username, editText_first, editText_last, editText_email, editText_phone, editText_address, editText_nip, editText_upin, editText_password, editText_password_confirm;
    ScrollView registrationScroll;
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


        editText_username = findViewById(R.id.editText_username);
        editText_first = findViewById(R.id.editText_first);
        editText_last = findViewById(R.id.editText_last);
        editText_email = findViewById(R.id.editText_email);
        editText_phone = findViewById(R.id.editText_phone);
        editText_address = findViewById(R.id.editText_address);
        editText_nip = findViewById(R.id.editText_nip);
        registrationScroll = findViewById(R.id.registrationScroll);
        editText_upin = findViewById(R.id.editText_upin);
        editText_upin.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                    registrationScroll.fullScroll(View.FOCUS_DOWN);

                    return true;
                }
                return false;
            }
        });
        editText_password = findViewById(R.id.editText_password);
        editText_password_confirm = findViewById(R.id.editText_password_confirm);
        progress_bar = findViewById(R.id.progress_bar);
        patient_register_button = findViewById(R.id.patient_register_button);

        globalUrlApi = new GlobalUrlApi();
        URL_Register = globalUrlApi.getNewBaseUrl() + "signup_from_app";
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


        try {

            JSONArray parent = new JSONArray(loadJSONFromAsset());
            stateNames = new String[parent.length() + 1];

            stateNames[0] = "Select Your State";
            for (int i = 0; i < parent.length(); i++) {


                JSONObject child = parent.getJSONObject(i);
                stateNames[i + 1] = child.getString("name");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


//        stateNames = new String[5];
//        stateNames[0] = "Select Your State";
//        stateNames[1] = "Alaska";
//        stateNames[2] = "Arizona Arkansas";
//        stateNames[3] = "California";
//        stateNames[4] = "Colorado";

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

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("states_json.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
        final String phone = editText_phone.getText().toString();
        final String address = editText_address.getText().toString();
        final String npi = editText_nip.getText().toString();
        final String upin = editText_upin.getText().toString();


        final String email = editText_email.getText().toString();
        final String password = editText_password.getText().toString();
        final String password_confirm = editText_password_confirm.getText().toString();
        final String username = editText_username.getText().toString();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (
                        Validations.isValidEmail(email)
                                && Validations.isPasswordConfirmMatched(password, password_confirm)
                                && Validations.isValidPassword(password)
                                && Validations.isUsernameValid(username)
                ) {


                    if (
                            username != null && !username.equals("") &&
                                    first != null && !first.equals("") &&
                                    last != null && !last.equals("") &&
                                    selected_date_of_birht != null && !selected_date_of_birht.equals("") &&
                                    email != null && !email.equals("") &&
                                    phone != null && !phone.equals("") &&
                                    address != null && !address.equals("") &&
                                    selected_state != null && !selected_state.equals("") &&
                                    password != null && !password.equals("")) {


                        if (
                                npi != null && !npi.equals("") &&
                                        upin != null && !upin.equals("") &&
                                        selected_spec != null && !selected_spec.equals("")
                        ) {

                            Register(username, first, last, selected_date_of_birht, email, phone,
                                    address, npi, upin,
                                    selected_state, selected_spec, password);
                        } else {
                            //Toast.makeText(DoctorRegisterActivity.this, "Please fill all fields (Professional Info)", Toast.LENGTH_SHORT).show();
                            String errors = "* Please fill all fields (Professional Info)";
                            ViewDialogRegistration viewDialog = new ViewDialogRegistration();
                            viewDialog.showDialog(DoctorRegisterActivity.this, errors);


                            progress_bar.setVisibility(View.GONE);
                            patient_register_button.setVisibility(View.VISIBLE);
                        }


                        //Toast.makeText(DoctorRegisterActivity.this, "All done", Toast.LENGTH_SHORT).show();

                    } else {
                        //Toast.makeText(DoctorRegisterActivity.this, "Please fill all fields (Personal Info)", Toast.LENGTH_SHORT).show();
                        String errors = "* Please fill all fields (Personal Info)";
                        ViewDialogRegistration viewDialog = new ViewDialogRegistration();
                        viewDialog.showDialog(DoctorRegisterActivity.this, errors);


                        progress_bar.setVisibility(View.GONE);
                        patient_register_button.setVisibility(View.VISIBLE);
                    }


                } else {
                    String errors = "";
                    if (!Validations.isValidEmail(email)) {
                        errors += "* Invalid Email\n\n";
                    }
                    if (!Validations.isPasswordConfirmMatched(password, password_confirm)) {
                        errors += "* Password Mismatched\n\n";
                    } else {
                        if (!Validations.isValidPassword(password)) {
                            errors += "* Password must contain minimum eight characters, at-least one letter, one number and one special character\n\n";
                        }
                    }
                    if (!Validations.isUsernameValid(username)) {
                        errors += "* Username must contain minimum eight characters, at-least one letter and one number\n\n";
                    }

                    ViewDialogRegistration viewDialog = new ViewDialogRegistration();
                    viewDialog.showDialog(DoctorRegisterActivity.this, errors);


                    progress_bar.setVisibility(View.GONE);
                    patient_register_button.setVisibility(View.VISIBLE);

                }


            }
        }, 10);


    }


    private void Register(String username, final String first, final String last, final String DOB, final String email, final String phone,
                          final String address, final String npi, final String upin,
                          final String state, final String spec, final String password) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_type", "doctor");
            jsonBody.put("username", username);
            jsonBody.put("name", first);
            jsonBody.put("last_name", last);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("date_of_birth", DOB);
            jsonBody.put("phone_number", phone);
            jsonBody.put("office_address", address);
            jsonBody.put("nip_number", npi);
            jsonBody.put("upin", upin);
            jsonBody.put("specialization", "1");
            jsonBody.put("country_id", "2");
            jsonBody.put("city_id", "3");
            jsonBody.put("state_id", "4");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("reg_response", response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            String status = jsonResponse.getString("Status");

                            if (status.equals("False")) {
                                patient_register_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorRegisterActivity.this, R.style.DialogTheme)
                                        .setTitle("Info!")
                                        .setMessage("Username or Email already registered")
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
                            } else if (status.equals("True")) {

                                patient_register_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorRegisterActivity.this, R.style.DialogTheme)
                                        .setTitle("info")
                                        .setMessage("Registration complete now please wait for an official approval of your account, Thank you.")
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

                            } else {
                                patient_register_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorRegisterActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("Error occured")
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


                            /*
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //  JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {

                                patient_register_button.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(PatientRegisterActivity.this, R.style.DialogTheme)
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
                                AlertDialog.Builder dialog = new AlertDialog.Builder(PatientRegisterActivity.this, R.style.DialogTheme)
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
                                AlertDialog.Builder dialog = new AlertDialog.Builder(PatientRegisterActivity.this, R.style.DialogTheme)
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
                            */


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
                        //Toast.makeText(PatientRegisterActivity.this, "VolleyError", Toast.LENGTH_SHORT).show();
                        // Register(email, password);
                        //    login_text.setVisibility(View.VISIBLE);
                        //   login_text.setText("Error from php");

                        patient_register_button.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorRegisterActivity.this, R.style.DialogTheme)
                                .setTitle("Warning!")
                                .setMessage("Username or Email already registered")
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
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_type", "patient");
                params.put("first_name", first);
                params.put("last_name", last);
                params.put("email", email);
                params.put("password", password);
                params.put("date_of_birth", selected_date_of_birht);
                params.put("phone_number", phone);
                params.put("address", address);
                params.put("state", selected_state);
                return params;
            }*/
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


        DatePickerDialog dialog = new DatePickerDialog(DoctorRegisterActivity.this,
                AlertDialog.THEME_HOLO_LIGHT,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();

    }

    public void setZipCityState(String zip, String city, String state, String abb) {
        TextView zip_TextView = findViewById(R.id.zip_TextView);
        TextView editText_city = findViewById(R.id.editText_city);
        TextView editText_state = findViewById(R.id.editText_state);

        zip_TextView.setText(zip);
        editText_city.setText(city);
        editText_state.setText(state);
        selected_state = state;

    }

    public void SearchZip(View view) {

        SearchZipStateCity searchZipStateCity = new SearchZipStateCity();
        searchZipStateCity.showSearchDialog(DoctorRegisterActivity.this);

    }
}