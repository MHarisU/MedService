package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.med.medservice.Models.OnlineDoctors.OnlineDoctorAdapter;
import com.med.medservice.Models.OnlineDoctors.OnlineDoctorsList;
import com.med.medservice.Models.SessionsVideoCall_OldApi.SessionsAdapter_OldApi;
import com.med.medservice.Models.SessionsVideoCall_OldApi.SessionsList_OldApi;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.ApiTokenCaller;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.OpenPrescribedItems;
import com.med.medservice.Utils.SearchLabDialog;
import com.med.medservice.Utils.SearchMedicineDialog;
import com.med.medservice.Utils.SearchPharmaciesDialog;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DoctorVideoActivity extends AppCompatActivity {

    WebView myWebView;

    private String TAG = "TEST";
    private PermissionRequest mPermissionRequest;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String[] PERM_CAMERA = {
            Manifest.permission.CAMERA
    };


    String name, user_id, role, user_email;
    SessionManager sessionManager;


    FirebaseDatabase rootNode;
    DatabaseReference reference;


    TextView patInfoButton;
    TextView prescriptionButton;
    TextView historyButton;
    TextView notesButton;

    boolean patInfoCheck = false;
    boolean prescriptionCheck = false;
    boolean historyCheck = false;
    boolean notesCheck = false;

    String pat_id;

    LinearLayout patInfoLayout, prescriptionLayout, historyLayout, notesLayout;

    CountDownTimer downTimer;


    ArrayList<SessionsList_OldApi> sessionsListOldApis;
    RecyclerView sessionRecycler;

    String symptoms_id, desc, session_id;

    String patient_name;
    String patient_age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_video);
        EmptyCart();

        getSessionID();

        SetupButton();

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        //    reference.setValue(uniqueId);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        role = user.get(sessionManager.USER_TYPE);
        user_id = user.get(sessionManager.ID);
        user_email = user.get(sessionManager.EMAIL);


        myWebView = (WebView) findViewById(R.id.webView1);
//chromeProgressBar = (ProgressBar) findViewById(R.id.progressBarChrome);
        //Settings
        WebSettings webSettings = myWebView.getSettings();

        myWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);

        //webSettings.setLoadsImagesAutomatically(true);
        //inizialize client

        //load website by URL

        //  myWebView.loadUrl("https://www.suunnoo.com/pages/r.html?room=tcgij5htuo&p=eyJsc1JlcFVybCI6Imh0dHBzOi8vdW1icmVsbGFtZC5jb20vdmlkZW9fY2hhdC8iLCJkaXNhYmxlVmlkZW8iOjAsImRpc2FibGVBdWRpbyI6MCwiZGlzYWJsZVNjcmVlblNoYXJlIjoxLCJkaXNhYmxlV2hpdGVib2FyZCI6MCwiZGlzYWJsZVRyYW5zZmVyIjoxLCJhdXRvQWNjZXB0VmlkZW8iOjEsImF1dG9BY2NlcHRBdWRpbyI6MSwiaXNBZG1pbiI6MX0&isAdmin=1");
        myWebView.loadUrl("https://www.suunnoo.com/pages/r.html?room=abcd123&p=eyJsc1JlcFVybCI6Imh0dHBzOi8vd3d3LnN1dW5ub28uY29tLyIsImRpc2FibGVWaWRlbyI6MCwiZGlzYWJsZUF1ZGlvIjowLCJkaXNhYmxlU2NyZWVuU2hhcmUiOjEsImRpc2FibGVXaGl0ZWJvYXJkIjowLCJkaXNhYmxlVHJhbnNmZXIiOjEsImF1dG9BY2NlcHRWaWRlbyI6MSwiYXV0b0FjY2VwdEF1ZGlvIjoxfQ&isAdmin=1");
        //register token for notification

        // this.onStart();
        // chromeProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        myWebView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam

            @Override
            public void onPermissionRequest(final PermissionRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

                        List<String> permissions = new ArrayList<String>();

                        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                            permissions.add(Manifest.permission.CAMERA);

                        }
                        if (!permissions.isEmpty()) {
                            requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
                        }
                    }

                }
            }
        });

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);
                super.onPageFinished(view, url);

                if (url.equals("https://suunnoo.com/pages/1")) {

                    final Dialog dialog = new Dialog(DoctorVideoActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.text_dialog_ok);

                    TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                    text.setText("Video call session with patient is ended");

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(DoctorVideoActivity.this, PrescriptionActivity.class);
                            EditText diagnosisView = findViewById(R.id.diagnosisView);
                            EditText notesView = findViewById(R.id.notesView);
                            intent.putExtra("diagnosis", diagnosisView.getText().toString());
                            intent.putExtra("notes", notesView.getText().toString());
                            intent.putExtra("session_id", session_id);
                            startActivity(intent);
                            finish();
                        }
                    });

                    try {
                        dialog.show();
                    } catch (WindowManager.BadTokenException e) {
                        //use a log message
                    }

                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                } else {
                    startTimer();
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });


        //GetPatientDetails();
        getPatientAndSessionDetails();

    }

    private void getPatientAndSessionDetails() {

        new ApiTokenCaller(DoctorVideoActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getPatientDetailForSession?session_id=" + session_id,
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {
                        Log.d("token_api_response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");

                            JSONObject data = jsonResponse.getJSONObject("Data");


                            patient_name = data.getString("patient_name");
                            patient_age = data.getString("patient_age");
                            setPatNameAndAge(patient_name, patient_age);


                            JSONObject symptoms = data.getJSONObject("symptoms");
                            setSymptomsView(symptoms);

                            JSONArray medical_profile = data.getJSONArray("medical_profile");
                            JSONObject medical_profile_obj = medical_profile.getJSONObject(0);
                            //Log.d("token_api_response", medical_profile.toString());
                            setPatientMedicalProfile(medical_profile_obj);

                            JSONArray visit_history = data.getJSONArray("visit_history");
                            Log.d("Visit_history", visit_history.toString());
                            setupPrevisionSessionHistoryView(visit_history);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        );

    }

    private void setupPrevisionSessionHistoryView(JSONArray visit_history) throws JSONException {

        sessionRecycler = findViewById(R.id.sessionsRecycler);
        sessionRecycler.setLayoutManager(new LinearLayoutManager(this));
        sessionsListOldApis = new ArrayList<SessionsList_OldApi>();


        for (int i = 0; i < visit_history.length(); i++) {
            JSONObject child = visit_history.getJSONObject(i);
            String session_id = child.getString("session_id");
            String doctor_id = user_id;
            String date = child.getString("created_at");
            String name = child.getString("doctor_first_name");
            String last_name = child.getString("doctor_last_name");

            sessionsListOldApis.add(new SessionsList_OldApi(session_id, doctor_id, date, name, last_name));


        }


        SessionsAdapter_OldApi adapter = new SessionsAdapter_OldApi(sessionsListOldApis, DoctorVideoActivity.this);
        sessionRecycler.setAdapter(adapter);

    }

    private void setPatientMedicalProfile(JSONObject medical_profile) throws JSONException {
        String user_id = medical_profile.getString("user_id");
        String allergies = medical_profile.getString("allergies");
        String previous_symp = medical_profile.getString("previous_symp");
        String family_history = medical_profile.getString("family_history");


        TextView allergiesView = findViewById(R.id.allergiesView);
        TextView preSympView = findViewById(R.id.preSympView);
        TextView famHistoryView = findViewById(R.id.famHistoryView);


        if (allergies != null && !allergies.equals("")) {
            allergiesView.setText(allergies);

        }
        if (previous_symp != null && !previous_symp.equals("")) {
            previous_symp = previous_symp.replace(",", "\n");
            preSympView.setText(previous_symp);

        }
        if (family_history != null && !family_history.equals("")) {

            family_history = family_history.replace("[", "");
            family_history = family_history.replace("]", "");
            family_history = family_history.replace("{", "");
            family_history = family_history.replace("\"", "");
            family_history = family_history.replace(",", " - ");
            family_history = family_history.replace("};", "\n\n");
            family_history = family_history.replace(":", " : ");
            family_history = family_history.replace("}", "\n");

            famHistoryView.setText(family_history);

        }


    }

    private void setSymptomsView(JSONObject symptoms) throws JSONException {
        String symptom_id = symptoms.getString("symptom_id");
        String headache = symptoms.getString("symptoms_headache");
        String fever = symptoms.getString("symptoms_fever");
        String flu = symptoms.getString("symptoms_flu");
        String nausea = symptoms.getString("symptoms_nausea");
        String others = symptoms.getString("symptoms_others");
        String symptoms_description = symptoms.getString("symptoms_description");

        String symps = "";
        if (headache.equals("1"))
            symps = symps + "\nHeadache";

        if (fever.equals("1"))
            symps = symps + "\nFever";

        if (flu.equals("1"))
            symps = symps + "\nFlu";

        if (nausea.equals("1"))
            symps = symps + "\nNausea";

        if (others.equals("1"))
            symps = symps + "\nOther";

        if (symptoms_description != null && !symptoms_description.equals(""))
            symps = symps + "\nDescription: " + symptoms_description;

        TextView symptomView = findViewById(R.id.symptomView);
        symptomView.setText(symps);
    }

    private void setPatNameAndAge(String patient_name, String patient_age) {
        TextView nameView = findViewById(R.id.nameView);
        TextView dateView = findViewById(R.id.dateView);

        nameView.setText(patient_name);
        dateView.setText(patient_age);
    }

    private void getSessionID() {
        Intent intent = getIntent();
        session_id = intent.getStringExtra("session_id");
    }

    private void GetPatientDetails() {
        reference.child("calling").child(user_id).child("incoming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {


                    pat_id = snapshot.getValue().toString();

                    GetPatientAge(pat_id);
                    GetPatientMedicalHistory(pat_id);
                    GetSymptops(pat_id, user_id);
                    GetCurrentSession(pat_id, user_id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetCurrentSession(final String pat_id, String user_id) {

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_session.php?" +
                "patient_id=" + pat_id +
                "&doctor_id=" + user_id,
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
                                    String session_id = child.getString("id");

                                    //Toast.makeText(DoctorVideoActivity.this, ""+session_id, Toast.LENGTH_SHORT).show();
                                    GetAllSessions(pat_id, session_id);

                                    //Toast.makeText(SendInvitationActivity.this, ""+session_id, Toast.LENGTH_LONG).show();


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

    private void GetAllSessions(String pat_id, String session_id) {

        sessionRecycler = findViewById(R.id.sessionsRecycler);
        sessionRecycler.setLayoutManager(new LinearLayoutManager(this));
        sessionsListOldApis = new ArrayList<SessionsList_OldApi>();

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_all_sessions_except_current.php?" +
                "patient_id=" + pat_id +
                "&session_id=" + session_id,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            Log.d("SessionsAll", response);

                            try {

                                JSONArray parent = new JSONArray(response);
                                // doctorNames = new String[parent.length()];
                                //  doctorId = new String[parent.length()];
                                // doctorNames[0] = "Select Doctor";
                                //  doctorId[0] = "";

                                for (int i = 0; i < parent.length(); i++) {
                                    JSONObject child = parent.getJSONObject(i);
                                    String session_id = child.getString("id");
                                    String doctor_id = child.getString("doctor_id");
                                    String date = child.getString("date");
                                    String name = child.getString("name");
                                    String last_name = child.getString("last_name");

                                    sessionsListOldApis.add(new SessionsList_OldApi(session_id, doctor_id, date, name, last_name));


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                        SessionsAdapter_OldApi adapter = new SessionsAdapter_OldApi(sessionsListOldApis, DoctorVideoActivity.this);
                        sessionRecycler.setAdapter(adapter);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void GetSymptops(String pat_id, String user_id) {

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_symptoms.php?" +
                "patient_id=" + pat_id +
                "&doctor_id=" + user_id,
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
                                    String symptom_id = child.getString("id");
                                    String headache = child.getString("headache");
                                    String fever = child.getString("fever");
                                    String flu = child.getString("flu");
                                    String nausea = child.getString("nausea");
                                    String others = child.getString("others");

                                    String symps = "";
                                    if (headache.equals("1"))
                                        symps = symps + "\nHeadache";

                                    if (fever.equals("1"))
                                        symps = symps + "\nFever";

                                    if (flu.equals("1"))
                                        symps = symps + "\nFlu";

                                    if (nausea.equals("1"))
                                        symps = symps + "\nNausea";

                                    if (others.equals("1"))
                                        symps = symps + "\nOther";

                                    TextView symptomView = findViewById(R.id.symptomView);
                                    symptomView.setText(symps);


                                    //Toast.makeText(SendInvitationActivity.this, ""+session_id, Toast.LENGTH_LONG).show();


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

    private void GetPatientMedicalHistory(String pat_id) {

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_pat_medical_history.php?id=" + pat_id,
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
                                    String id = child.getString("id");
                                    String allergies = child.getString("allergies");
                                    String previous_symp = child.getString("previous_symp");
                                    String family_history = child.getString("family_history");

                                    TextView allergiesView = findViewById(R.id.allergiesView);
                                    TextView preSympView = findViewById(R.id.preSympView);
                                    TextView famHistoryView = findViewById(R.id.famHistoryView);


                                    if (allergies != null && !allergies.equals("")) {
                                        allergiesView.setText(allergies);

                                    }
                                    if (previous_symp != null && !previous_symp.equals("")) {
                                        previous_symp = previous_symp.replace(",", "\n");
                                        preSympView.setText(previous_symp);

                                    }
                                    if (family_history != null && !family_history.equals("")) {

                                        family_history = family_history.replace("{", "");
                                        family_history = family_history.replace("\"", "");
                                        family_history = family_history.replace(",", " - ");
                                        family_history = family_history.replace("};", "\n\n");
                                        family_history = family_history.replace(":", " : ");

                                        famHistoryView.setText(family_history);

                                    }


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

    private void GetPatientAge(String pat_id) {


        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_user_details.php?id=" + pat_id,
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
                                    String id = child.getString("id");
                                    String name = child.getString("name");
                                    String last_name = child.getString("last_name");
                                    String date_of_birth = child.getString("date_of_birth");

                                    TextView nameView = findViewById(R.id.nameView);
                                    TextView dateView = findViewById(R.id.dateView);


                                    nameView.setText(name + " " + last_name);
                                    dateView.setText(date_of_birth);


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

    private void EmptyCart() {
        CartDBHelper mydb;
        mydb = new CartDBHelper(this);
        mydb.removeAllItems(0);

    }

    private void SetupButton() {
        patInfoButton = findViewById(R.id.patInfoButton);
        prescriptionButton = findViewById(R.id.prescriptionButton);
        historyButton = findViewById(R.id.historyButton);
        notesButton = findViewById(R.id.notesButton);


        patInfoLayout = findViewById(R.id.patInfoLayout);
        prescriptionLayout = findViewById(R.id.prescriptionLayout);
        historyLayout = findViewById(R.id.historyLayout);
        notesLayout = findViewById(R.id.notesLayout);


    }

    private void startTimer() {

        final TextView timerView = findViewById(R.id.timerView);

        downTimer = new CountDownTimer(360000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerView.setText("" + String.format("Remaining %dm : %ds",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                EndSessionGlobal();
            }
        };

        downTimer.start();
    }

    public void EndSession(View view) {

        downTimer.cancel();
        EndSessionGlobal();
    }

    private void EndSessionGlobal() {
        reference.child("calling").child(user_id).child("isAvailable").setValue(false);
        myWebView.loadUrl("https://suunnoo.com/pages/1");

        /*
        final Dialog dialog = new Dialog(DoctorVideoActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.text_dialog_ok);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText("Video call session with patient is ended");

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(DoctorVideoActivity.this, PrescriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        */
    }

    public void PrescribeNow(View view) {

        PopupMenu menu = new PopupMenu(this, view);

        menu.getMenu().add("Medicine");
        menu.getMenu().add("Labs");
        menu.getMenu().add("Imaging");


        //registering popup with OnMenuItemClickListener
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {


                if (item.getTitle().equals("Medicine")) {

                    //Toast.makeText(DoctorVideoActivity.this, "Medicines", Toast.LENGTH_SHORT).show();
                    SearchMedicineDialog searchMedicineDialog = new SearchMedicineDialog();
                    searchMedicineDialog.showSearchMedicineDialog(DoctorVideoActivity.this, session_id);

                }
                if (item.getTitle().equals("Labs")) {
                    //  Intent intent = new Intent(DoctorVideoActivity.this, UploadProfileActivity.class);
                    //  intent.putExtra("image", user_image);
                    //  startActivity(intent);
                    SearchLabDialog searchLabDialog = new SearchLabDialog();
                    searchLabDialog.showSearchLabsDialog(DoctorVideoActivity.this);


                }
                if (item.getTitle().equals("Imaging")) {
                    //   Intent intent = new Intent(DoctorVideoActivity.this, ChangePasswordActivity.class);
                    //   startActivity(intent);

                }

                return true;
            }
        });

        menu.show(); //showing popup menu

    }

    public void OpenPatientDetails(View view) {


        if (!patInfoCheck) {

            GetPatientAge(pat_id);
            GetPatientMedicalHistory(pat_id);

            patInfoLayout.setVisibility(View.VISIBLE);
            prescriptionLayout.setVisibility(View.GONE);
            historyLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.GONE);


            patInfoButton.setBackgroundResource(R.drawable.red_bg);
            patInfoButton.setTextColor(Color.WHITE);
            patInfoCheck = true;
            prescriptionCheck = false;
            historyCheck = false;
            notesCheck = false;
        } else {
            patInfoLayout.setVisibility(View.GONE);
            prescriptionLayout.setVisibility(View.GONE);
            historyLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.GONE);


            patInfoButton.setBackgroundColor(Color.WHITE);
            patInfoButton.setTextColor(Color.BLACK);
            patInfoCheck = false;
            prescriptionCheck = false;
            historyCheck = false;
            notesCheck = false;
        }

        prescriptionButton.setBackgroundColor(Color.WHITE);
        prescriptionButton.setTextColor(Color.BLACK);

        historyButton.setBackgroundColor(Color.WHITE);
        historyButton.setTextColor(Color.BLACK);

        notesButton.setBackgroundColor(Color.WHITE);
        notesButton.setTextColor(Color.BLACK);

    }

    public void OpenPrescriptions(View view) {


        if (!prescriptionCheck) {
            patInfoLayout.setVisibility(View.GONE);
            prescriptionLayout.setVisibility(View.VISIBLE);
            historyLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.GONE);

            OpenPrescribedItems prescribedItems = new OpenPrescribedItems(DoctorVideoActivity.this, "DoctorVideoActivity");

            prescriptionButton.setBackgroundResource(R.drawable.red_bg);
            prescriptionButton.setTextColor(Color.WHITE);
            prescriptionCheck = true;
            patInfoCheck = false;
            historyCheck = false;
            notesCheck = false;
        } else {
            patInfoLayout.setVisibility(View.GONE);
            prescriptionLayout.setVisibility(View.GONE);
            historyLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.GONE);


            prescriptionButton.setBackgroundColor(Color.WHITE);
            prescriptionButton.setTextColor(Color.BLACK);
            prescriptionCheck = false;
            patInfoCheck = false;
            historyCheck = false;
            notesCheck = false;
        }


        patInfoButton.setBackgroundColor(Color.WHITE);
        patInfoButton.setTextColor(Color.BLACK);

        historyButton.setBackgroundColor(Color.WHITE);
        historyButton.setTextColor(Color.BLACK);

        notesButton.setBackgroundColor(Color.WHITE);
        notesButton.setTextColor(Color.BLACK);
    }

    public void OpenHistory(View view) {

        if (!historyCheck) {
            patInfoLayout.setVisibility(View.GONE);
            prescriptionLayout.setVisibility(View.GONE);
            historyLayout.setVisibility(View.VISIBLE);
            notesLayout.setVisibility(View.GONE);


            historyButton.setBackgroundResource(R.drawable.red_bg);
            historyButton.setTextColor(Color.WHITE);
            historyCheck = true;
            patInfoCheck = false;
            prescriptionCheck = false;
            notesCheck = false;
        } else {

            patInfoLayout.setVisibility(View.GONE);
            prescriptionLayout.setVisibility(View.GONE);
            historyLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.GONE);


            historyButton.setBackgroundColor(Color.WHITE);
            historyButton.setTextColor(Color.BLACK);
            historyCheck = false;

            patInfoCheck = false;
            prescriptionCheck = false;
            notesCheck = false;
        }

        patInfoButton.setBackgroundColor(Color.WHITE);
        patInfoButton.setTextColor(Color.BLACK);

        prescriptionButton.setBackgroundColor(Color.WHITE);
        prescriptionButton.setTextColor(Color.BLACK);

        notesButton.setBackgroundColor(Color.WHITE);
        notesButton.setTextColor(Color.BLACK);
    }

    public void OpenNotes(View view) {

        if (!notesCheck) {

            patInfoLayout.setVisibility(View.GONE);
            prescriptionLayout.setVisibility(View.GONE);
            historyLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.VISIBLE);

            notesButton.setBackgroundResource(R.drawable.red_bg);
            notesButton.setTextColor(Color.WHITE);
            notesCheck = true;
            patInfoCheck = false;
            prescriptionCheck = false;
            historyCheck = false;
        } else {

            patInfoLayout.setVisibility(View.GONE);
            prescriptionLayout.setVisibility(View.GONE);
            historyLayout.setVisibility(View.GONE);
            notesLayout.setVisibility(View.GONE);

            notesButton.setBackgroundColor(Color.WHITE);
            notesButton.setTextColor(Color.BLACK);
            notesCheck = false;
            patInfoCheck = false;
            prescriptionCheck = false;
            historyCheck = false;
        }

        patInfoButton.setBackgroundColor(Color.WHITE);
        patInfoButton.setTextColor(Color.BLACK);

        prescriptionButton.setBackgroundColor(Color.WHITE);
        prescriptionButton.setTextColor(Color.BLACK);

        historyButton.setBackgroundColor(Color.WHITE);
        historyButton.setTextColor(Color.BLACK);

    }

    public void FirstDec(View view) {
        startActivity(new Intent(getApplicationContext(), SessionDetailActivity.class));
    }

    public void AddLab(View view) {
    }

    public void AddPharmacy(View view) {
        SearchPharmaciesDialog searchPharmaciesDialog = new SearchPharmaciesDialog();
        searchPharmaciesDialog.showSearchLabsDialog(DoctorVideoActivity.this);
    }

    public void setPharmacy(String selected_pharmacy_id, String selected_pharmacy_name, String selected_pharmacy_address) {
        LinearLayout pharmacyLayout = findViewById(R.id.pharmacyLayout);
        TextView pharmacySelectedNameView = findViewById(R.id.pharmacySelectedNameView);

        pharmacySelectedNameView.setText(selected_pharmacy_name + ", " + selected_pharmacy_address);
        pharmacyLayout.setVisibility(View.VISIBLE);

    }


    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /*
    @Override
    public void onBackPressed()
    {
        if(myWebView.canGoBack())
            myWebView.goBack();
        else
            super.onBackPressed();
    }

     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downTimer.cancel();
    }
}