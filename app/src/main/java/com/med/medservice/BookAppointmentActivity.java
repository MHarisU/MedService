package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import com.med.medservice.Diaglogs.ViewDialogActivityClose;
import com.med.medservice.NetworkAPI.ApiCallerNew;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Diaglogs.SymptomSelectorDialogFragment;
import com.med.medservice.Diaglogs.ViewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookAppointmentActivity extends AppCompatActivity implements SymptomSelectorDialogFragment.onMultiChoiceListener {

    String[] doctorNames;
    String[] doctorId;
    String selectedDoctorId;
    String selectedDoctorName;
    Spinner doctorSpinner, spinner_time, monthSpinner;
    String[] timeSlots;
    ArrayList<String> slotArray;

    String selectedDate, selectedDay, selectedTime, SelectedSymp;


    TextView symtoms_text;


    String name, user_id, email, phone;
    SessionManager sessionManager;

    TextView emailView, nameView;

    LinearLayout appointmentLayout;
    CardView cardProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_appointment);

        sessionManager = new SessionManager(this);
        //sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        phone = user.get(sessionManager.PHONE);
        user_id = user.get(sessionManager.ID);

        emailView = findViewById(R.id.emailView);
        nameView = findViewById(R.id.nameView);
        emailView.setText(email);
        nameView.setText(name);

        symtoms_text = findViewById(R.id.symtoms_text);
        doctorSpinner = findViewById(R.id.spinner_doctor);
        spinner_time = findViewById(R.id.spinner_time);


        appointmentLayout = findViewById(R.id.appointmentLayout);
        cardProgress = findViewById(R.id.cardProgress);

        // SetupDoctorSpinner();

        GetDoctors();

    }

    private void GetAvailability(final String selectDate) {


        //old api
        /*ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_doctor_availability.php?" +
                "doctor_id=" + selectedDoctorId +
                "&datee=" + selectDate,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            Log.d("Availabilities", response);

                            try {

                                JSONArray parent = new JSONArray(response);
                                // doctorNames = new String[parent.length()];
                                // doctorId = new String[parent.length()];

                                // doctorNames[0] = "Select Doctor";
                                //  doctorId[0] = "";

                                for (int i = 0; i < parent.length(); i++) {
                                    JSONObject child = parent.getJSONObject(i);
                                    String id = child.getString("id");
                                    String slotStartTime = child.getString("slotStartTime");
                                    String slotEndTime = child.getString("slotEndTime");


                                    String firstDate = "26/02/2019";
                                    String firstTime = "10:00 AM";
                                    String secondDate = "26/02/2019";
                                    String secondTime = "11:00 AM";

                                    String format = "yyyy-dd-MM HH:mm:SS";

                                    SimpleDateFormat sdf = new SimpleDateFormat(format);

                                    Date dateObj1 = sdf.parse(selectDate + " " + slotStartTime);
                                    Date dateObj2 = sdf.parse(selectDate + " " + slotEndTime);
                                    // Date dateObj1 = sdf.parse( slotStartTime);
                                    //  Date dateObj2 = sdf.parse( slotEndTime);
                                    System.out.println("Date Start: " + dateObj1);
                                    System.out.println("Date End: " + dateObj2);

                                    slotArray = new ArrayList<>();
                                    long dif = dateObj1.getTime();
                                    while (dif <= dateObj2.getTime()) {
                                        Date slot = new Date(dif);

                                        // SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss");
                                        SimpleDateFormat form = new SimpleDateFormat("hh:mm a");
                                        slotArray.add("" + form.format(slot));

                                        System.out.println("Minute Slot ---> " + form.format(slot) + " SIZE:" + slotArray.size());
                                        dif += 600000;
                                    }


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }

                        //   doctorNames = new String[5];
                        //   doctorNames[0] = "Select Doctor";
                        //   doctorNames[1] = "Dr. Jane Edward";
                        //  doctorNames[2] = "Dr. Saleem Javed";
                        //   doctorNames[3] = "Dr. Imran Ali";
                        //  doctorNames[4] = "Dr. Aftab Khan";

                        // SetupDoctorSpinner();

                        try {

                            timeSlots = new String[slotArray.size()];

                            for (int jj = 0; jj < slotArray.size(); jj++) {
                                timeSlots[jj] = slotArray.get(jj);
                            }
                            SetupTimeSpinner();
                        } catch (NullPointerException e) {
                            ViewDialog viewDialog = new ViewDialog();
                            viewDialog.showDialog(BookAppointmentActivity.this, "No timeslots available on this date");
                        }

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

        new ApiTokenCaller(BookAppointmentActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getAppointmentDoctorsAvailability?" +
                "doctor_id=" +selectedDoctorId+
                "&date=" +selectDate,
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


                                String slotStartTime = child.getString("slotStartTime");
                                String slotEndTime = child.getString("slotEndTime");


                                String format = "yyyy-dd-MM HH:mm:SS";

                                SimpleDateFormat sdf = new SimpleDateFormat(format);

                                Date dateObj1 = sdf.parse(selectDate + " " + slotStartTime);
                                Date dateObj2 = sdf.parse(selectDate + " " + slotEndTime);
                                // Date dateObj1 = sdf.parse( slotStartTime);
                                //  Date dateObj2 = sdf.parse( slotEndTime);
                                System.out.println("Date Start: " + dateObj1);
                                System.out.println("Date End: " + dateObj2);

                                slotArray = new ArrayList<>();
                                long dif = dateObj1.getTime();
                                while (dif <= dateObj2.getTime()) {
                                    Date slot = new Date(dif);

                                    // SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss");
                                    SimpleDateFormat form = new SimpleDateFormat("hh:mm a");
                                    slotArray.add("" + form.format(slot));

                                    System.out.println("Minute Slot ---> " + form.format(slot) + " SIZE:" + slotArray.size());
                                    dif += 600000;
                                }


                            }

                            try {

                                timeSlots = new String[slotArray.size()];

                                for (int jj = 0; jj < slotArray.size(); jj++) {
                                    timeSlots[jj] = slotArray.get(jj);
                                }
                                SetupTimeSpinner();
                            } catch (NullPointerException e) {
                                ViewDialog viewDialog = new ViewDialog();
                                viewDialog.showDialog(BookAppointmentActivity.this, "No timeslots available on this date");
                            }


                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );

    }


    private void GetDoctors() {


        //old api
        /*ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_doctors.php",
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            // Log.d("ApiResponse", response);

                            try {

                                JSONArray parent = new JSONArray(response);
                                doctorNames = new String[parent.length()];
                                doctorId = new String[parent.length()];

                                // doctorNames[0] = "Select Doctor";
                                //  doctorId[0] = "";

                                for (int i = 0; i <= parent.length(); i++) {
                                    JSONObject child = parent.getJSONObject(i);
                                    String id = child.getString("id");
                                    String name = child.getString("name");
                                    String last_name = child.getString("last_name");

                                    doctorNames[i] = name + " " + last_name;
                                    doctorId[i] = id;


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }

                        //   doctorNames = new String[5];
                        //   doctorNames[0] = "Select Doctor";
                        //   doctorNames[1] = "Dr. Jane Edward";
                        //  doctorNames[2] = "Dr. Saleem Javed";
                        //   doctorNames[3] = "Dr. Imran Ali";
                        //  doctorNames[4] = "Dr. Aftab Khan";

                        SetupDoctorSpinner();

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

        new ApiTokenCaller(BookAppointmentActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getAppointmentDoctors?patient_id="+ new SessionManager(BookAppointmentActivity.this).getUserId(),
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {
                        Log.d("token_api_response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");

                            JSONArray arrayData = jsonResponse.getJSONArray("Data");


                            doctorNames = new String[arrayData.length()];
                            doctorId = new String[arrayData.length()];

                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject child = arrayData.getJSONObject(i);


                                String doctor_id = child.getString("doctor_id");
                                String doctor_name = child.getString("doctor_name");

                                doctorNames[i] = "Dr."+doctor_name;
                                doctorId[i] = doctor_id;


                            }

//
//                            if (doctorNames.length>0){
//                                cardProgress.setVisibility(View.GONE);
//                                appointmentLayout.setVisibility(View.VISIBLE);
//                            }
//                            else {
//                                ViewDialogActivityClose activityClose = new ViewDialogActivityClose();
//                                activityClose.showDialog(BookAppointmentActivity.this, "You can not book an appointment, please goto E-Visit first, thank you");
//                                cardProgress.setVisibility(View.GONE);
//                            }

                            SetupDoctorSpinner();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );

    }


    private void SetupTimeSpinner() {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, timeSlots);

        spinner_time.setAdapter(adapter);

        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = spinner_time.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                //  if (positions != 0)
                //selectedDoctorId = timeSlots[positions];
                selectedTime = timeSlots[positions];
                //Toast.makeText(BookAppointmentActivity.this, "" + timeSlots[positions], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    private void SetupDoctorSpinner() {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, doctorNames);

        doctorSpinner.setAdapter(adapter);

        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = doctorSpinner.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                //  if (positions != 0)
                selectedDoctorId = doctorId[positions];
                selectedDoctorName = doctorNames[positions];
                //Toast.makeText(BookAppointmentActivity.this, "" + doctorId[positions], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void Close(View view) {
        finish();
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
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                //Toast.makeText(BookAppointmentActivity.this, "" + dayOfWeek, Toast.LENGTH_SHORT).show();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                date_time.setText("Date: " + simpleDateFormat.format(calendar.getTime()));

                if (dayOfWeek == 1){
                    selectedDay ="Sunday";
                }
                else if (dayOfWeek == 2){
                    selectedDay ="Monday";
                }
                else if (dayOfWeek == 3){
                    selectedDay ="Tuesday";
                }
                else if (dayOfWeek == 4){
                    selectedDay ="Wednesday";
                }
                else if (dayOfWeek == 5){
                    selectedDay ="Thursday";
                }
                else if (dayOfWeek == 6){
                    selectedDay ="Friday";
                }
                else if (dayOfWeek == 7){
                    selectedDay ="Saturday";
                }

               // Toast.makeText(BookAppointmentActivity.this, ""+selectedDay, Toast.LENGTH_SHORT).show();

                selectedDate = simpleDateFormat.format(calendar.getTime());
                GetAvailability(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(BookAppointmentActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    public void SelectSymptoms(View view) {
        DialogFragment symptomsDialog = new SymptomSelectorDialogFragment();
        symptomsDialog.setCancelable(false);
        symptomsDialog.show(getSupportFragmentManager(), "Multichoice Dialog");
    }

    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedSymptoms) {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilders = new StringBuilder();
        stringBuilder.append("Selected Symptoms:");
        for (String str : selectedSymptoms) {
            stringBuilder.append("\n- " + str);
            stringBuilders.append(str + ",");

        }
        SelectedSymp = stringBuilders.toString();
        symtoms_text.setText(stringBuilder);

    }

    @Override
    public void onNegativeButtonClicked() {

        symtoms_text.setText("Select Symptoms");
    }

    public void AppointmentConfirm(View view) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(BookAppointmentActivity.this, R.style.DialogTheme)
                .setTitle("")
                .setMessage("Do you confirm this appointment?         ")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNeutralButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        //  homeIntent.addCategory(Intent.CATEGORY_HOME);
                        //  homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  startActivity(homeIntent);

                        OnlineBookAppointment();

                        // startActivity(n);
                        //   System.exit(0);
                    }
                });
        dialog.show();
    }

    private void OnlineBookAppointment() {

        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("patient_id", user_id);
            orderJsonObject.put("doctor_id", selectedDoctorId);
            orderJsonObject.put("patient_name", name);
            orderJsonObject.put("doctor_name", selectedDoctorName);
            orderJsonObject.put("email", email);
            orderJsonObject.put("phone", phone);

            orderJsonObject.put("date", selectedDate);
            orderJsonObject.put("time", selectedTime);
            orderJsonObject.put("problem", SelectedSymp);
            orderJsonObject.put("status", "pending");
            orderJsonObject.put("day", selectedDay);

            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = orderJsonObject.toString();
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, new GlobalUrlApi().getNewBaseUrl() + "createAppointment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("order_api_response", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                            //JSONObject jsonData = jsonResponse.getJSONObject("Data");
                            String jsonStatus = jsonResponse.getString("Status");

                            if (jsonStatus.equals("True")) {


                                final Dialog dialog = new Dialog(BookAppointmentActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Appointment Successfully Booked");

                                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();

                                    }
                                });

                                dialog.show();

                                Window window = dialog.getWindow();
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                            } else {


                                final Dialog dialog = new Dialog(BookAppointmentActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Appointment Not Booked");

                                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();

                                    }
                                });

                                dialog.show();

                                Window window = dialog.getWindow();
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //   login_button.setVisibility(View.VISIBLE);
                            //   progress_bar.setVisibility(View.GONE);
                            // Toast.makeText(LoginActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(BookAppointmentActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
                           // progressDialog.dismiss();
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
                        //Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                        //Login(email, password);
                        //    login_text.setVisibility(View.VISIBLE);
                        //   login_text.setText("Error from php");

                        //  login_button.setVisibility(View.VISIBLE);
                        // progress_bar.setVisibility(View.GONE);
                        //progressDialog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(BookAppointmentActivity.this, R.style.DialogTheme)
                                .setTitle("Warning!")
                                .setMessage("Volley Error")
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //  login_button.setVisibility(View.VISIBLE);
                                        //  progress_bar.setVisibility(View.GONE);

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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Bearer " + new SessionManager(BookAppointmentActivity.this).getToken();
                headers.put("Authorization", auth);
                return headers;
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


    private void OnlineBookAppointment2() {


        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "book_patient_appointment.php?" +
                "patient_id=" + user_id +
                "&patient_name=" + name +
                "&doctor_id=" + selectedDoctorId +
                "&doctor_name=" + selectedDoctorName +
                "&pat_email=" + email +
                "&pat_phone=0" +
                "&datee=" + selectedDate +
                "&timee=" + selectedTime +
                "&problem=" + (SelectedSymp)+
                "&day=" + (selectedDay),
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {

                            JSONObject object = new JSONObject(response);
                            if (object.getString("success").equals("1")) {

                                final Dialog dialog = new Dialog(BookAppointmentActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Appointment Successfully Booked");

                                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();

                                    }
                                });

                                dialog.show();

                                Window window = dialog.getWindow();
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                            } else {


                                final Dialog dialog = new Dialog(BookAppointmentActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Appointment Not Booked");

                                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();

                                    }
                                });

                                dialog.show();

                                Window window = dialog.getWindow();
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                            }


                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }

                        //   doctorNames = new String[5];
                        //   doctorNames[0] = "Select Doctor";
                        //   doctorNames[1] = "Dr. Jane Edward";
                        //  doctorNames[2] = "Dr. Saleem Javed";
                        //   doctorNames[3] = "Dr. Imran Ali";
                        //  doctorNames[4] = "Dr. Aftab Khan";

                        //   SetupDoctorSpinner();

                    }


                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }
}