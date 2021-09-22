package com.med.medservice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadProfileActivity extends AppCompatActivity {

    Button selectImg, uploadImg;
    ImageView profileView, uploadImageIcon;

    Bitmap bitmap;
    String encodedImage;

    String UserImageOrg;

    String user_id;
    SessionManager sessionManager;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);

        progressDialog = new ProgressDialog(UploadProfileActivity.this);

        selectImg = findViewById(R.id.selectImageButton);
        uploadImg = findViewById(R.id.uploadImageButton);
        profileView = findViewById(R.id.profileView);
        uploadImageIcon = findViewById(R.id.uploadImageIcon);

        Intent intent = getIntent();
        UserImageOrg = intent.getStringExtra("image");
        if (UserImageOrg != null && !UserImageOrg.equals("random-avatar4.jpg") && !UserImageOrg.equals("") ) {
            Picasso.get()
                    .load(new GlobalUrlApi().getHomeUrl()+"asset_admin/images/" + UserImageOrg)
                    // .placeholder(context.getResources().getDrawable(R.drawable.ic))
                    .into(profileView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
        }



        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        // name = user.get(sessionManager.FIRST_NAME);
        //  name = name + " " + user.get(sessionManager.LAST_NAME);
        //  email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dexter.withActivity(UploadProfileActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 10);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest request = new StringRequest(Request.Method.POST, "http://harisdev.com/medical/upload_profile.php"
                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(UploadProfileActivity.this, "" + response, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(UploadProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("image", encodedImage);
                        params.put("user_id", "1");
                        return params;

                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(UploadProfileActivity.this);
                requestQueue.add(request);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {

            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profileView.setImageBitmap(bitmap);
                uploadImageIcon.setVisibility(View.VISIBLE);
                uploadImg.setVisibility(View.VISIBLE);
                storeImage(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void storeImage(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imageByte = stream.toByteArray();
        encodedImage = android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);

        Log.d("ENCODE", encodedImage);

    }

    public void Finish(View view) {
        finish();
    }

    public void SelectPhoto(View view) {
        Dexter.withActivity(UploadProfileActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 10);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void uploadPhoto(View view) {

        progressDialog.setMessage("Uploading profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        UploadImage();

    }

    private void UploadImage() {

        StringRequest request = new StringRequest(Request.Method.POST, "http://harisdev.com/medical/upload_profile.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(UploadProfileActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                final Dialog dialog = new Dialog(UploadProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.text_dialog_ok);

                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText("Profile Changed");

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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                final Dialog dialog = new Dialog(UploadProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.text_dialog_ok);

                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText("Profile not changed");

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                       // finish();
                    }
                });

                dialog.show();

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage);
                params.put("user_id", user_id);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UploadProfileActivity.this);
        requestQueue.add(request);
    }
}