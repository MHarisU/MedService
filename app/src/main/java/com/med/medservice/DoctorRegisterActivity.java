package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.github.gcacace.signaturepad.views.SignaturePad;
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

    LinearLayout accountDetailsLayout, personalDetailsLayout, professionalDetailsLayout, termsDetailsLayout;

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

        accountDetailsLayout = findViewById(R.id.accountDetailsLayout);
        personalDetailsLayout = findViewById(R.id.personalDetailsLayout);
        professionalDetailsLayout = findViewById(R.id.professionalDetailsLayout);
        termsDetailsLayout = findViewById(R.id.termsDetailsLayout);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signCheck = true;
                return false;
            }
        });


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
        specNames[1] = "Primary Care";
        /*specNames[2] = "Dental";
        specNames[3] = "Kidney";
        specNames[4] = "Eye";
        specNames[5] = "Lung";
        specNames[6] = "Psychiatrist";
        specNames[7] = "Skin Care";*/

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

    public void accountDetailContinue(View view) {

       // LinearLayout accountDetailsLayout, personalDetailsLayout, professionalDetailsLayout, termsDetailsLayout;

        accountDetailsLayout.setVisibility(View.GONE);
        personalDetailsLayout.setVisibility(View.VISIBLE);

    }

    public void backToAccountDetail(View view) {


        personalDetailsLayout.setVisibility(View.GONE);
        accountDetailsLayout.setVisibility(View.VISIBLE);
    }

    public void continuePersonalDetail(View view) {

        personalDetailsLayout.setVisibility(View.GONE);
        professionalDetailsLayout.setVisibility(View.VISIBLE);
    }

    public void backToPersonalDetail(View view) {

        professionalDetailsLayout.setVisibility(View.GONE);
        personalDetailsLayout.setVisibility(View.VISIBLE);
    }

    public void continueProfessionalDetail(View view) {

        professionalDetailsLayout.setVisibility(View.GONE);
        termsDetailsLayout.setVisibility(View.VISIBLE);
    }

    public void backToProfessionalDetail(View view) {

        termsDetailsLayout.setVisibility(View.GONE);
        professionalDetailsLayout.setVisibility(View.VISIBLE);
    }

    public void viewAllTerms(View view) {
        TextView termsOfUse = findViewById(R.id.termsOfUse);
        TextView viewTerms = findViewById(R.id.viewTerms);
        viewTerms.setText("");
        termsOfUse.setText("This website (“Site”) is owned and operated by Umbrella Health Care Systems (“Umbrella,” “we,” “our,” or “us”). The Site connects users with healthcare providers for virtual telehealth services and provides telemedicine solutions to users including access to pharmacy, laboratory testing, imaging, substance abuse, and dermatology services (the “Services”). \n" +
                "\n" +
                "UMBRELLA IS NOT INTENDED TO PROVIDE MEDICAL ADVICE, DIAGNOSIS, OR SUGGESTIONS FOR TREATMENT. YOU CONTACT YOUR PHYSICIAN OR OTHER HEALTHCARE PROFESSIONAL CONCERNING YOUR PARTICULAR MEDICAL CONDITION OR CONCERNS. DO NOT DISREGARD PROFESSIONAL MEDICAL ADVICE, OR DELAY IN SEEKING IT BECAUSE OF SOMETHING YOU READ ON UMBRELLA. \n" +
                "\n" +
                "PLEASE DO NOT ATTEMPT TO COMMUNICATE WITH US ABOUT YOUR SPECIFIC MEDICAL CONDITION, EVEN IN AN EMERGENCY, THROUGH UMBRELLA. WE DO NOT MONITOR YOUR COMMUNICATIONS TO US THROUGH THE SITE FOR THESE PURPOSES, AND WE MAY NOT RESPOND TO YOUR COMMUNICATION IN AN APPROPRIATE TIMEFRAME. CONTACT YOUR PHYSICIAN OR HEALTHCARE PROFESSIONAL FOR ADVICE CONCERNING YOUR MEDICAL NEEDS OR CONTACT YOUR LOCAL EMERGENCY SERVICES IN CASE OF AN EMERGENCY.  \n" +
                "THESE TERMS OF USE (“TERMS”) CONSTITUTE A BINDING AGREEMENT BETWEEN YOU AND US. PLEASE READ CAREFULLY THROUGH ALL SECTIONS OF THESE TERMS. YOUR ACCESS TO AND USE OF THE SITE IS SUBJECT TO THESE TERMS AND ALL APPLICABLE LAWS AND WE RESERVE THE RIGHT TO TERMINATE YOUR ACCESS TO THE SITE IF YOU VIOLATE THESE TERMS. BY CLICKING ON LINKS WITHIN THE SITE OR WEBPAGES BEYOND THE SITE’S HOMEPAGE OR BY CLICKING ON A BOX OR ICON YOU AGREE TO THESE TERMS WHETHER OR NOT YOU COMPLETE A TRANSACTION WITH UMBRELLA AND WHETHER OR NOT YOU COMPLETE YOUR TRANSACTION ON THE SITE OR THROUGH OTHER CHANNELS, SUCH AS BY TELEPHONE, EMAIL, FACSIMILE OR OTHERWISE. IF YOU DO NOT AGREE WITH THESE TERMS, DO NOT ACCESS OR OTHERWISE USE THE SITE, ANY SERVICES AVAILABLE THROUGH THIS SITE, OR ANY INFORMATION CONTAINED ON THIS SITE.\n" +
                "\n" +
                "MANDATORY ARBITRATION NOTICE AND CLASS ACTION AND JURY TRIAL WAIVER. These Terms contain a mandatory (binding) arbitration provision and class action and jury trial waiver clauses. Except for certain types of disputes described in the arbitration section below or where prohibited by applicable law, you agree that disputes between you and us regarding your use of the Site or Services will be resolved by binding, individual arbitration and you waive your right to participate in a class action lawsuit or class-wide arbitration, including as a class representative. The arbitrator’s decision will be subject to very limited review by a court. You will be entitled to a fair hearing, but the arbitration procedures are simpler and more limited than rules applicable in Court. For more details, see below.\n" +
                "We may make changes to the content available on the Site at any time. We can change, update, add, or remove provisions of these Terms at any time by posting the updated Terms on the Site. We will make commercially reasonable efforts to notify you of any material changes to these Terms however we are not obligated to. You waive any right you may have to receive specific notice of such changes to these Terms except for changes to our agreement to arbitration, which is discussed more fully below. By using the Site after we have updated the Terms, you are agreeing to the then-current Terms. You are responsible for regularly reviewing these Terms.\n" +
                "\n" +
                "In addition to these Terms, your use of certain Services may be governed by additional agreements.\n" +
                "\n" +
                "Besides these Terms, we also publish a Privacy Policy [hyperlink]. Although it is not part of these Terms, we encourage you to read it to better understand how you can update, manage, access, and delete your information.\n" +
                "\n" +
                " \n" +
                "\n" +
                "Accessing the Site\n" +
                "We reserve the right to withdraw or amend this Site, and any Services or Materials (defined below) we provide on the Site, in our sole discretion and without notice. We will not be liable if, for any reason, all or any part of the Site is unavailable at any time or for any period. From time to time, in our sole discretion and without notice, we may restrict access to some parts of the Site, or the entire Site, to users, including registered users.\n" +
                "You are responsible for both:\n" +
                "Making all arrangements necessary for you to have access to the Site.\n" +
                "Ensuring that all persons who access the Site through your internet connection are aware of these Terms and comply with them.\n" +
                "To access the Site or certain of the resources it offers, you may be asked to provide certain registration details or other information. It is a condition of your use of the Site that all the information you provide on the Site is correct, current, and complete, and that you have the authority to provide such information to us. \n" +
                "If you choose, or are provided with, a username, password, or any other piece of information as part of our security procedures, you must treat such information as confidential, and you must not disclose it to any other person or entity. You also acknowledge that your account is personal to you and agree not to provide any other person with access to this Site or portions of it using your username, password, or other security information. You agree to notify us immediately of any unauthorized access to or use of your username or password or any other breach of security. You also agree to ensure that you sign out of or exit from your account at the end of each session. You should use particular caution when accessing your account from a public or shared computer so that others are not able to view or record your password or other personal information.\n" +
                "We have the right to disable any username, password, or other identifier, whether chosen by you or provided by us, at any time in our sole discretion for any reason, including if, in our opinion, you have violated any provision of these Terms.\n" +
                "\n" +
                "Our Role\n" +
                "Healthcare providers contracted with Umbrella (“Providers”) provide consultations and services through telecommunication technologies. The Provider conducting the consultation determines the scope of services for diagnosis, treatment, and care. \n" +
                "\n" +
                "Our role is limited to making certain telehealth related information available to you and helping facilitate your access to telemedicine and medical services. Umbrella is independent from Providers and is not responsible for such Providers’ acts, communications, or any content provided by them. \n" +
                "\n" +
                "We do not engage in the practice of medicine. We do not provide medical advice. The decision to follow treatment recommendations rests with you and your Provider. By accessing, using, or browsing the Site or Services or providing medical history, you do not create a physician-patient or healthcare provider-patient relationship between you and Umbrella or any of our employees and/or affiliates. Any Services provided by us, or content accessed by you on the Site are for informational purposes only and are not intended to replace the advice of your healthcare provider. Please consult your doctor or other qualified healthcare provider if you have any questions about a medical condition. Call 911 for all medical emergencies. UMBRELLA IS NOT RESPONSIBLE OR LIABLE FOR ANY ADVICE, COURSE OF TREATMENT, DIAGNOSIS OR ANY OTHER INFORMATION, SERVICES OR PRODUCTS THAT YOU MAY OBTAIN THROUGH THE SITE. \n" +
                "\n" +
                "The information presented on or through the Site is made available solely for general information purposes. The information provided does not constitute medical advice or counsel. We do not warrant the accuracy, completeness, or usefulness of this information. Any reliance you place on such information is strictly at your own risk. We disclaim all liability and responsibility arising from any reliance placed on such materials by you or any other visitor to the Site, or by anyone who may be informed of any of its contents.\n" +
                "This Site includes content provided by third parties, including materials provided by other users, bloggers, and third-party licensors, syndicators, aggregators, and/or reporting services. All statements and/or opinions expressed in these materials, and all articles and responses to questions and other content, other than the content provided by us, are solely the opinions and the responsibility of the person or entity providing those materials. These materials do not necessarily reflect the opinion of Umbrella. We are not responsible, or liable to you or any third party, for the content or accuracy of any materials provided by any third parties.\n" +
                "\n" +
                "Proprietary Rights and Your Use of the Site\n" +
                "Unless otherwise specified in these Terms, all information and screens appearing on this Site are the sole property of Umbrella or our subsidiaries and affiliates, and other parties. We provide content through the Site that is copyrighted or contains protectable trademarks of Umbrella or our third-party licensors and suppliers (collectively, the “Materials”). Materials may include documents, services, software, site design, text, graphics, logos, video, images, icons, and other content, as well as the arrangement thereof. \n" +
                "Subject to these Terms, we hereby grant to you a revocable, limited, personal, non-exclusive, and non-transferable license to use, view, print, display, and download the Materials for the sole purpose of viewing them on a stand-alone personal computer or mobile device and to use this Site solely for your personal use. Except for the foregoing license and as otherwise required or limited by applicable law, you have no other rights in the Site or any Materials and you may not modify, edit, copy, reproduce, create derivative works of, reverse engineer, alter, enhance, or in any way exploit any of the Site or Materials in any manner or for any purpose that would constitute infringement of our, our licensors’, or the Site’s other user’s intellectual property rights. All rights not expressly granted herein are reserved. \n" +
                "If you breach any of these Terms, the above license will terminate automatically and you must immediately destroy any downloaded or printed Materials.\n" +
                "\n" +
                "Your Communications to the Site\n" +
                "By forwarding any content or communications to us through the Site or by other electronic means, you thereby grant us a perpetual, royalty-free, fully paid-up, world-wide, irrevocable, nonexclusive, freely transferable, and freely sublicensable license to use, reproduce, modify, adapt, publish, translate, create derivative works from, redistribute, and display such content and communications in any form for the purposes of providing the Services and any purpose tangentially related to the Services. No compensation will be paid to you with respect to our or our sublicensees’ use of your communications. By providing or submitting content, you represent and warrant that you own or otherwise control all of the rights to your submitted content and communications as described in this section, including all the rights necessary for you to submit the content and communications and grant the license above.\n" +
                "\n" +
                "Electronic Communications\n" +
                "By using the Site and/or the Services, you consent to receiving electronic communications, including electronic notices, from us. These electronic communications may include notices about applicable fees and charges, transactional information and other information concerning or related to the Site and/or Materials. These electronic communications are part of your relationship with us. You agree that any notices, agreements, disclosures or other communications that we send you electronically will satisfy any legal communication requirements, including that such communications be in writing.\n" +
                "\n" +
                "Permitted Uses\n" +
                "By accessing or using the Site, you agree that:\n" +
                "\n" +
                "Your use of the Site is subject to and governed by these Terms;\n" +
                "You will only access or use the Site and transact business with us if you are at least eighteen (18) years old;\n" +
                "You will use the Site solely for its Services offered in the normal course of business;\n" +
                "You will always act in accordance with the law and custom, and in good faith;\n" +
                "You will comply with and be bound by these Terms as they appear on the Site each time you access and use the Site;\n" +
                "Each use of the Site by you indicates and confirms your agreement to be bound by these Terms; and\n" +
                "These Terms are a legally binding agreement between you and us that will be enforceable against you.\n" +
                "You further agree to not use the Site in any way that:\n" +
                "\n" +
                " \n" +
                "\n" +
                "Changes or alters the Site or content or Services that may appear on the Site;\n" +
                "Impairs in any way the integrity or operation of the Site;\n" +
                "Interferes with or induces a breach of the contractual relationships between us and our employees;\n" +
                "Is in any way unlawful or prohibited, or that is harmful or destructive to anyone or their property;\n" +
                "Transmits any advertisements, solicitations, schemes, spam, flooding, or other unsolicited email and commercial communications;\n" +
                "Transmits any harmful or disabling computer codes or viruses;\n" +
                "Harvests email addresses from the Site;\n" +
                "Transmits unsolicited email to the Site or to anyone whose email address includes the domain name of the Site;\n" +
                "Interferes with our network services;\n" +
                "Attempts to gain unauthorized access to our network services;\n" +
                "Suggests an express or implied affiliation or relationship with us without our express written permission;\n" +
                "Impairs or limits our ability to operate the Site or any other person’s ability to access and use the Site;\n" +
                "Unlawfully impersonates or otherwise misrepresents your affiliation with any person or entity;\n" +
                "Transmits or uploads violent, obscene, sexually explicit, discriminatory, hateful, threatening, abusive, defamatory, offensive, harassing, or otherwise objectionable content or images;\n" +
                "Dilutes or depreciates our or any of our affiliates’ name and reputation;\n" +
                "Transmits or uploads content or images that infringe upon any third party’s intellectual property rights or right to privacy; or\n" +
                "Unlawfully transmits or uploads any confidential, proprietary or trade secret information.\n" +
                "Umbrella has no obligation, but maintains the right, to monitor the Site. This list of prohibited activities provides examples and is not complete or exclusive. We reserve the right to terminate access to your account and your ability to use this Site (or the Materials) with or without cause and with or without notice, for any reason or no reason, or for any action that we determine is inappropriate or disruptive to this Site or to any other user of this Site and/or Materials. We may report to law enforcement authorities any actions that may be illegal, and any reports it receives of such conduct. When legally required or at our discretion, we will cooperate with law enforcement agencies in any investigation of alleged illegal activity on this Site or on the Internet, which may include disclosing any information we obtain. In addition, we may disclose information we obtain as necessary or appropriate to operate or improve the Site, to protect Umbrella and/or our Site users, or for any other purpose that the law permits.\n" +
                "\n" +
                "Third-Party Links\n" +
                "This Site may link to other websites that are not sites controlled or operated by us (collectively, “Third-Party Sites”). You acknowledge and agree that the Third-Party Sites may have different privacy policies and terms and conditions and/or user guides and business practices than Umbrella, and you further acknowledge and agree that your use of such Third-Party Sites is governed by the respective Third-Party Site privacy policy and terms and conditions and/or user guides. We provide links to the Third-Party Sites to you as a convenience, and we do not verify, make any representations or take responsibility for such Third-Party Sites, including the truthfulness, accuracy, quality or completeness of the content, services, links displayed and/or any other activities conducted on or through such Third-Party Sites. YOU AGREE THAT WE WILL NOT, UNDER ANY CIRCUMSTANCES, BE RESPONSIBLE OR LIABLE, DIRECTLY OR INDIRECTLY, FOR ANY GOODS, SERVICES, INFORMATION, RESOURCES AND/OR CONTENT AVAILABLE ON OR THROUGH ANY THIRD-PARTY SITES AND/OR THIRD-PARTY DEALINGS OR COMMUNICATIONS, OR FOR ANY HARM RELATED THERETO, OR ANY DAMAGES OR LOSSES CAUSED OR ALLEGED TO BE CAUSED BY OR IN CONNECTION WITH YOUR USE OR RELIANCE ON THE CONTENT OR BUSINESS PRACTICES OF ANY THIRD-PARTY. Any reference on the Site to any product, service, publication, institution, or organization of any third-party entity or individual does not constitute or imply our endorsement or recommendation.\n" +
                "\n" +
                "Linking to the Site and Social Media Features\n" +
                "You may link to our Site, provided you do so in a way that is fair and legal and does not damage our reputation or take advantage of it, but you must not establish a link in such a way as to suggest any form of association, approval, or endorsement on our part without our express written consent. \n" +
                "This Site may provide certain social media features that enable you to:\n" +
                "\n" +
                "Link from your own or certain third-party websites to certain content on this Site.\n" +
                "Send emails or other communications with certain content, or links to certain content, on this Site.\n" +
                "Cause limited portions of content on this Site to be displayed or appear to be displayed on your own or certain third-party websites.\n" +
                "You may use these features solely as they are provided by us and otherwise in accordance with any additional terms and conditions we provide with respect to such features. Subject to the foregoing, you must not:\n" +
                "\n" +
                "Establish a link from any website that is not owned by you.\n" +
                "Cause the Site or portions of it to be displayed on, or appear to be displayed by, any other site, for example, framing, deep linking, or in-line linking.\n" +
                "Link to any part of the Site other than the homepage.\n" +
                "Otherwise take any action with respect to the materials on this Site that is inconsistent with any other provision of these Terms.\n" +
                "The website from which you are linking, or on which you make certain content accessible, must comply in all respects with these Terms.\n" +
                "You agree to cooperate with us to stop any unauthorized framing or linking immediately. We reserve the right to withdraw linking permission without notice.\n" +
                "We may disable all or any social media features and any links at any time in our sole discretion and without notice. \n" +
                "\n" +
                "Federal and State Laws \n" +
                "The Site is operated from the U.S. and is intended for U.S. residents only. The Site is not approved for distribution outside of the U.S. and non-U.S. residents should not rely or act upon the information contained within. When using the Site, on the Site, or when using any content provided by us, you must obey all applicable U.S. federal, state, and local laws. \n" +
                "\n" +
                "Minimum Age\n" +
                "We do not allow persons under the age of eighteen (18) to use the Site. By using the Site, you represent and warrant that you are eighteen (18) years of age or over.\n" +
                "\n" +
                "Disclaimer of Warranties \n" +
                "Your use of this Site is at your own risk. The Materials have not been verified or authenticated in whole or in part by us, and they may include inaccuracies or typographical or other errors. We do not warrant the accuracy or timeliness of the Materials contained on this Site. We have no liability for any errors or omissions in the Materials, whether provided by us, our licensors or suppliers or other users. \n" +
                "TO THE FULLEST EXTENT PROVIDED BY LAW AND EXCEPT AS OTHERWISE PROVIDED HEREIN OR ON THE SITE, THE INFORMATION AND SERVICES OFFERED ON OR THROUGH THE SITE AND ANY REFERENCED THIRD-PARTY SITE ARE PROVIDED “AS IS” AND WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED. ANY THIRD-PARTY GOODS OR SERVICES PROVIDED ARE SUPPLIED AS A CONVENIENCE TO YOU AND DO NOT CONSTITUTE SPONSORSHIP, AFFILIATION, PARTNERSHIP, OR ENDORSEMENT. TO THE FULLEST EXTENT ALLOWED BY LAW, WE DISCLAIM ALL EXPRESS AND IMPLIED WARRANTIES, INCLUDING THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT.\n" +
                "TO THE FULLEST EXTENT ALLOWED BY LAW, WE DO NOT WARRANT OR MAKE ANY REPRESENTATIONS REGARDING THE USE OR THE RESULTS OF THE USE OF THE SITE, THE MATERIALS, ANY CONTENT, OR OTHER POSTED MATERIALS ON THE SITE IN TERMS OF ITS CORRECTNESS, ACCURACY, TIMELINESS, RELIABILITY OR OTHERWISE.\n" +
                "BY PROVIDING THE SERVICES ON THE SITE, WE DO NOT IN ANY WAY PROMISE THAT THE SERVICES WILL REMAIN AVAILABLE TO YOU. WE ARE ENTITLED TO TERMINATE ALL OR PART OF ANY OF THE SITE AT ANY TIME, IN OUR SOLE DISCRETION WITHOUT NOTICE TO YOU.\n" +
                "\n" +
                "Limitation of Liability\n" +
                "WE cannot guarantee the Site will be available one hundred percent (100%) of the time Because public networks, such as the internet, occasionally experience disruptions. Although WE STRIVE to provide the most reliable webSite reasonably possible, interruptions and delays in accessing the Site are unavoidable and WE DISCLAIM any liability for damages resulting from such problems.\n" +
                "\n" +
                "NOTWITHSTANDING THE FOREGOING, THE LIABILITY OF UMBRELLA AND ITS AFFILIATES, EMPLOYEES, AGENTS, REPRESENTATIVES AND THIRD-PARTY SERVICE PROVIDERS WITH RESPECT TO ANY AND ALL CLAIMS ARISING OUT OF YOUR USE OF THE SITE, THE MATERIALS, AND ANY CONTENT OR SERVICES OBTAINED THROUGH THE SITE, WHETHER BASED ON WARRANTY, CONTRACT, NEGLIGENCE, STRICT LIABILITY OR OTHERWISE, SHALL NOT EXCEED, IN THE AGGREGATE, FIFTY DOLLARS ($50). \n" +
                "IN NO EVENT WILL WE BE LIABLE TO YOU OR ANY PARTY FOR ANY DIRECT, INDIRECT, SPECIAL OR OTHER CONSEQUENTIAL DAMAGES FOR ANY USE OF THE SITE, OR ON ANY OTHER HYPERLINKED WEBSITE, INCLUDING, WITHOUT LIMITATION, ANY LOST PROFITS, BUSINESS INTERRUPTION, LOSS OF PROGRAMS OR OTHER DATA OR OTHERWISE, EVEN IF WE ARE EXPRESSLY ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. \n" +
                "\n" +
                "Indemnification\n" +
                "You agree to indemnify, defend and hold harmless us and our affiliates, employees, agents, representatives and third-party service providers, for any and all claims, demands, actions, liability, fines, penalties and expenses that may arise from any of your acts through the use of the Site. Such acts may include: (i) providing content to or communicating with us or our Affiliates; (ii) unauthorized use of material obtained through the Site; (iii) engaging in a prohibited activity; or (iv) any other action that breaches these Terms. \n" +
                "\n" +
                "Copyright Complaints \n" +
                "Umbrella respects the intellectual property of others. If you believe that your work has been copied in a way that constitutes copyright infringement, please contact us through the contact information provided in these Terms.\n" +
                "\n" +
                "Injunctive Relief\n" +
                "You acknowledge that we may be irreparably damaged if these Terms are not specifically enforced, and damages at law would be an inadequate remedy. Therefore, in the event of a breach or threatened breach of any provision of these Terms by you, we shall be entitled, without prejudice to any other rights and remedies that may be sought under Section 20, to an injunction restraining such breach or threatened breach, without being required to show any actual damage or to post an injunction bond, and/or to a decree for specific performance of the provisions of these Terms. For purposes of this Section, you agree that any action or proceeding with regard to such injunction restraining such breach or threatened breach shall be brought in the state or federal courts located in Missouri. You consent to the jurisdiction of such court and waive any objection to the laying of venue of any such action or proceeding in such court. You agree that service of any court paper may be effected on such party by mail or in such other manner as may be provided under applicable laws, rules of procedure or local rules.\n" +
                "\n" +
                "MANDATORY ARBITRATION AND CLASS ACTION AND JURY TRIAL WAIVER.\n" +
                "Most concerns can be resolved quickly and to your satisfaction by contacting us as set forth in the “Questions” section below.\n" +
                "In the event that we are not able to resolve a dispute, and with the exception of the claims for injunctive relief by us as described above and otherwise set forth herein, you hereby agree that either you or we may require any dispute, claim, or cause of action (“Claim”) between you and us or any third parties arising out of use of the Site, the Services, and any other actions with us (whether based in contract, tort, statute, fraud, misrepresentation, or any other legal theory) to be arbitrated on an individual (non-class) basis. However, both parties retain the right to seek relief in a small claims court (or a state court equivalent) for a Claim within the scope of its jurisdiction so long as the small claims action does not seek to certify a class, combine the claims of multiple persons, recover damages in excess of the limit for a small claim under applicable state law or is not transferred, removed, or appealed from small claims court to any different court. Additionally, if you are a California resident, you retain the right to obtain public injunctive relief from any court with proper jurisdiction. \n" +
                "\n" +
                "THERE IS NO JUDGE OR JURY IN ARBITRATION, AND COURT REVIEW OF AN ARBITRATION AWARD IS VERY LIMITED. ADDITIONALLY, ANY ARBITRATION OF A CLAIM WILL BE ON AN INDIVIDUAL BASIS, AND, THEREFORE, YOU UNDERSTAND AND AGREE THAT YOU ARE WAIVING THE RIGHT TO PARTICIPATE AS A CLASS REPRESENTATIVE OR CLASS MEMBER IN A CLASS ACTION LAWSUIT. AS PART OF THIS WAIVER, YOU AGREE THAT YOU WAIVE THE RIGHT TO ACT AS A PRIVATE ATTORNEY GENERAL IN AN ARBITRATION; THAT EXCEPT AS OTHERWISE PROVIDED IN THIS ARBITRATION AGREEMENT, CLAIMS BROUGHT BY OR AGAINST YOU MAY NOT BE JOINED OR CONSOLIDATED WITH CLAIMS BROUGHT BY OR AGAINST ANY OTHER PERSON; AND THE ARBITRATOR SHALL HAVE NO AUTHORITY TO CONDUCT A CLASS-WIDE ARBITRATION, PRIVATE ATTORNEY GENERAL ARBITRATION OR MULTIPLE-PARTY ARBITRATION.\n" +
                "\n" +
                "\n" +
                "You and we agree that your use of the Services involves interstate commerce, and that this arbitration agreement shall be interpreted and enforced in accordance with the Federal Arbitration Act (FAA) set forth in Title 9 of the U.S. Code to the fullest extent possible, notwithstanding any state law to the contrary, regardless of the origin or nature of the Claims at issue. The arbitrator must follow, to the extent applicable: (1) the substantive law of the state in which we entered into the transaction giving rise to this arbitration agreement; (2) the applicable statutes of limitations; and (3) claims of privilege recognized at law. The arbitrator will not be bound by federal, state or local rules of procedure and evidence or by state or local laws concerning arbitration proceedings.\n" +
                "If either you or we elect to arbitrate a Claim, the dispute shall be resolved by binding arbitration administered under the applicable rules of the American Arbitration Association (“AAA”). Either you or we may elect to resolve a particular Claim through arbitration, even if the other party has already initiated litigation in court related to the Claim, by: (a) making written demand for arbitration upon the other party, (b) initiating arbitration against the other party, or (c) filing a motion to compel arbitration in court.\n" +
                "If this is a consumer-purpose transaction, the applicable rules will be the AAA’s Consumer Arbitration Rules. The applicable AAA rules and other information about arbitrating a claim under AAA, including how to submit a dispute to arbitration, may be obtained by visiting its website at https://www.adr.org/ or by calling 1-800-778-7879. If AAA will not serve as the administrator of the arbitration, and you and we cannot then agree upon a substitute arbitrator, you and we shall request that a court with proper jurisdiction appoint an arbitrator. However, we will abide by the applicable AAA rules regardless of the forum. Arbitration shall be conducted in the county and state where you accepted these Terms, you reside, or another reasonably convenient place to you as determined by the arbitrator, unless applicable laws require another location. Judgment on the award rendered by the arbitrator may be entered in any court having jurisdiction thereof. Except as provided in applicable statutes, the arbitrator’s award is not subject to review by the court and it cannot be appealed. The parties will have the option to request and receive a statement of reasons for the arbitration award.\n" +
                "If you elect to file the arbitration, and this is a consumer-purpose transaction, you will pay the filing fee to the extent required by AAA’s Consumer Arbitration Rules but not to exceed the cost of filing a lawsuit. Any amount above what it would cost you to file a lawsuit, we will pay. All other arbitration fees and expenses shall be allocated to us according to AAA rules. Except for the arbitration fees and expenses, each party shall pay its own costs and fees incurred (including attorneys’ fees), unless the arbitrator allocates them differently in accordance with applicable law. This paragraph applies only if this is a consumer-purpose transaction.\n" +
                "Notwithstanding anything to the contrary in these Terms, and except as otherwise set forth in this paragraph, the agreement to arbitration may be amended by us only upon advance notice to you. If we make any amendment to this agreement to arbitration (other than renumbering the agreement to align with any other amendment to the Terms) in the future, that amendment shall not apply to any claim that was filed in a legal proceeding or action against us prior to the effective date of the amendment. The amendment shall apply to all other Claims governed by this agreement to arbitration that have arisen or may arise between you and us. However, we may amend this agreement to arbitration and not provide you notice; in that case, the amendments will not apply to you and the agreement to arbitration contained in these Terms to which you agreed will continue to apply to you and us as if no amendments were made.  \n" +
                "If any part of this arbitration provision is invalid, all other parts of it remain valid. However, if the class action limitation is invalid, then this arbitration provision is invalid in its entirety, provided that the remaining Terms shall remain in full force and effect. This arbitration provision will survive the termination of your use of the Site, the Services, and any other actions with us. \n" +
                "\n" +
                "\n" +
                "You may reject this arbitration provision within thirty (30) days of accepting the Terms by emailing us at _____________________ and including in the subject line “Rejection of Arbitration Provision.”\n" +
                "\n" +
                "Miscellaneous Provisions\n" +
                "Severability. If any term or provision in these Terms is found to be void, against public policy, or unenforceable by a court of competent jurisdiction and such finding or order becomes final with all appeals exhausted, then the offending provision shall be deemed modified to the extent necessary to make it valid and enforceable. If the offending provision cannot be so modified, then the same shall be deemed stricken from these Terms in its entirety and the remainder of these Terms shall survive with the said offending provision eliminated.\n" +
                "\n" +
                "Governing Law and Venue. These Terms shall be governed by and construed in accordance with the laws of the State of Missouri, excluding its conflicts of law rules, and the United States of America. Except as set forth in the agreement to arbitration and without waiving it, you agree that any dispute arising from or relating to the subject matter of these Terms (including but not limited to if you opt out of the agreement to arbitration) shall be governed by the exclusive jurisdiction and venue of the state and federal courts of St. Louis, Missouri, except where the jurisdiction and venue are mandated by applicable assignment. \n" +
                "\n" +
                "Assignment. We may freely assign our obligations and rights under these Terms, including all personal information in our possession that we have collected during your use of the Site as further described in our Privacy Policy [hyperlink].\n" +
                "\n" +
                "No Waiver. \u200BNo failure, omission or delay on the part of Umbrella in exercising any right under these Terms will preclude any other further exercise of that right or other right under these Terms.\n" +
                "\n" +
                "Headings. Provision and section headings are for convenience of reference only and shall not affect the interpretation of these Terms.\n" +
                "\n" +
                "Typographical Errors. Information on the Site may contain technical inaccuracies or typographical errors. We attempt to make the Site’s postings as accurate as possible, but we do not warrant the content of the Site is accurate, complete, reliable, current, or error-free. \n" +
                "\n" +
                "Questions\n" +
                "If you have any questions or comments about these Terms or this Site, please contact us by email at [INSERT EMAIL ADDRESS]. You also may write to us at: \n" +
                "\n" +
                "Umbrella Health Care Systems\n" +
                "Attn:[Insert Title of Contact]\n" +
                "6800 Olive Blvd\n" +
                "Suite C, St. Louis, MO 63130\n" +
                "[Insert Email-Address]");


    }


    SignaturePad mSignaturePad;
    boolean signCheck = false;
    public void clearPad(View view) {
        signCheck = false;

        mSignaturePad.clear();
    }
}