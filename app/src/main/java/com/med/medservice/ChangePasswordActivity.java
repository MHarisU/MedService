package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView editText_password, editText_password_c;

    ProgressDialog progressDialog;

    String newPassword;

    String user_id;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        progressDialog = new ProgressDialog(ChangePasswordActivity.this);

        editText_password = findViewById(R.id.editText_password);
        editText_password_c = findViewById(R.id.editText_password_c);


        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        // name = user.get(sessionManager.FIRST_NAME);
        //  name = name + " " + user.get(sessionManager.LAST_NAME);
        //  email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);

    }

    public void Finish(View view) {
        finish();
    }

    public void ChangePassword(View view) {

        if ( editText_password.getText().toString().equals(editText_password_c.getText().toString())
                && !editText_password.getText().toString().equals("")){

            newPassword = editText_password.getText().toString();


            progressDialog.setMessage("Changing Password...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "/update_password.php?" +
                    "id=" +user_id+
                    "&password=" +newPassword,
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
                                progressDialog.dismiss();

                                final Dialog dialog = new Dialog(ChangePasswordActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Password Changed");

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
                               // PatientProfileActivity.getInstance().UpdateSuccessful();
                            }else {
                                progressDialog.dismiss();

                                final Dialog dialog = new Dialog(ChangePasswordActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.text_dialog_ok);

                                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                                text.setText("Password not changed");

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



                        }

                    });

            // asyncTask.execute();
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
        else {
            Toast.makeText(this, "Enter Password Correctly", Toast.LENGTH_SHORT).show();
        }

    }
}