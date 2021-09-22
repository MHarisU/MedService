package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date_time.setText("" + simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(EditProfileActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    public void Update(View view) {

        UpdateOnline();

        /*
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
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
    }

    private void UpdateOnline() {

        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("name", nameView.getText().toString());
            orderJsonObject.put("last_name", nameLastView.getText().toString());
            orderJsonObject.put("date_of_birth", dateView.getText().toString());
            orderJsonObject.put("phone_number", phoneView.getText().toString());
            orderJsonObject.put("office_address", addressView.getText().toString());

            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = orderJsonObject.toString();
        // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, new GlobalUrlApi().getNewBaseUrl() +
                "updateUserProfile/"+user_id,
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

                                finish();
                                PatientProfileActivity.getInstance().UpdateSuccessful();


                            } else {

                               // progressDialog.dismiss();
                                final Dialog dialog = new Dialog(EditProfileActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Info not updated");

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
                            Toast.makeText(EditProfileActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileActivity.this, R.style.DialogTheme)
                                .setTitle("Warning!")
                                .setMessage("Volley Error\n"+error.toString())
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
                String auth = "Bearer " + new SessionManager(EditProfileActivity.this).getToken();
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

}