package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.med.medservice.Diaglogs.ViewDialog;
import com.med.medservice.Utils.SessionManager;

import java.util.HashMap;

public class DoctorProfileActivity extends AppCompatActivity {


    LinearLayout personal_info, earning_info, patient_timeline;

    ImageView timelineButton, earningButton, profileButton;

    TextView nameView, dateView, emailView, phoneView, addressView;
    ImageView profileView;
    CardView personalViewCard;
    ProgressBar progressBarProfile;

    LinearLayout august_month, july_month;
    boolean august_month_check = false;
    boolean july_month_check = false;


    String user_id;
    SessionManager sessionManager;

    String name;
    String last_name;
    String full_name;
    String email;
    String date_of_birth;
    String phone_number;
    String office_address;
    String user_image;


    private static DoctorProfileActivity instance;

    public static DoctorProfileActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_profile);
        instance = this;


        personal_info = findViewById(R.id.personal_info);
        earning_info = findViewById(R.id.earning_info);
        patient_timeline = findViewById(R.id.patient_timeline);

        profileButton = findViewById(R.id.profileButton);
        earningButton = findViewById(R.id.earningButton);
        timelineButton = findViewById(R.id.timelineButton);

        august_month = findViewById(R.id.august_month);
        july_month = findViewById(R.id.july_month);

        LoadProfile();

    }

    private void LoadProfile() {

        sessionManager = new SessionManager(this);
        final HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);
        date_of_birth = user.get(sessionManager.DOB);
        phone_number = user.get(sessionManager.PHONE);
        office_address = user.get(sessionManager.ADDRESS);


        progressBarProfile = findViewById(R.id.progressBarProfile);
        personalViewCard = findViewById(R.id.personalViewCard);
        profileView = findViewById(R.id.profileView);
        addressView = findViewById(R.id.addressView);
        phoneView = findViewById(R.id.phoneView);
        emailView = findViewById(R.id.emailView);
        dateView = findViewById(R.id.dateView);
        nameView = findViewById(R.id.nameView);

        nameView.setText(name);
        emailView.setText(email);
        dateView.setText(date_of_birth);
        phoneView.setText(phone_number);
        addressView.setText(office_address);


        //loading online profile
        /*ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "/get_user_details.php?id=" + user_id,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            //  Log.d("ApiResponse", response);

                            try {

                                JSONArray parent = new JSONArray(response);

                                for (int i = 0; i < parent.length(); i++) {
                                    JSONObject child = parent.getJSONObject(i);
                                    name = child.getString("name");
                                    last_name = child.getString("last_name");
                                    full_name = name + " " + last_name;
                                    email = child.getString("email");
                                    date_of_birth = child.getString("date_of_birth");
                                    phone_number = child.getString("phone_number");
                                    office_address = child.getString("office_address");
                                    user_image = child.getString("user_image");


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }

                        progressBarProfile.setVisibility(View.GONE);
                        personalViewCard.setVisibility(View.VISIBLE);

                        nameView.setText(full_name);
                        dateView.setText(date_of_birth);
                        emailView.setText(email);
                        phoneView.setText(phone_number);
                        addressView.setText(office_address);


                        if (user_image != null && !user_image.equals("random-avatar4.jpg") && !user_image.equals("")) {

                            Picasso.get()
                                    .load(new GlobalUrlApi().getHomeUrl() + "asset_admin/images/" + user_image)
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

                    }

                });
        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/


    }


    public void Close(View view) {
        finish();
    }

    public void ProfileClick(View view) {
        personal_info.setVisibility(View.VISIBLE);
        earning_info.setVisibility(View.GONE);
        patient_timeline.setVisibility(View.GONE);

        profileButton.setBackgroundColor(Color.parseColor("#2c98f0"));
        earningButton.setBackgroundColor(Color.parseColor("#CC111111"));
        timelineButton.setBackgroundColor(Color.parseColor("#CC111111"));


    }

    public void EarningReportClick(View view) {
        personal_info.setVisibility(View.GONE);
        earning_info.setVisibility(View.VISIBLE);
        patient_timeline.setVisibility(View.GONE);

        profileButton.setBackgroundColor(Color.parseColor("#CC111111"));
        earningButton.setBackgroundColor(Color.parseColor("#2c98f0"));
        timelineButton.setBackgroundColor(Color.parseColor("#CC111111"));
    }

    public void TimelineClick(View view) {
        personal_info.setVisibility(View.GONE);
        earning_info.setVisibility(View.GONE);
        patient_timeline.setVisibility(View.VISIBLE);

        profileButton.setBackgroundColor(Color.parseColor("#CC111111"));
        earningButton.setBackgroundColor(Color.parseColor("#CC111111"));
        timelineButton.setBackgroundColor(Color.parseColor("#2c98f0"));
    }

    public void ClickAugust(View view) {

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        if (!august_month_check) {
            august_month.setVisibility(View.VISIBLE);
            august_month.startAnimation(slide_down);
            august_month_check = true;
        } else {
            august_month.startAnimation(slide_up);

            august_month.setVisibility(View.GONE);
            august_month_check = false;
        }
    }

    public void ClickJuly(View view) {

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        if (!july_month_check) {
            july_month.setVisibility(View.VISIBLE);
            july_month.startAnimation(slide_down);
            july_month_check = true;
        } else {
            july_month.startAnimation(slide_up);

            july_month.setVisibility(View.GONE);
            july_month_check = false;
        }
    }

    public void UpdateSuccessful() {
        ViewDialog dialog = new ViewDialog();
        dialog.showDialog(DoctorProfileActivity.this, "Updated Successfully...");
    }

    public void settingProfile(View view) {
        PopupMenu menu = new PopupMenu(this, view);

        //   menu.getMenu().add("Change Profile");
        menu.getMenu().add("Edit Account");
        menu.getMenu().add("Change Password");


        //registering popup with OnMenuItemClickListener
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {


                if (item.getTitle().equals("Edit Account")) {
                    Intent intent = new Intent(DoctorProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("first_name", name);
                    intent.putExtra("last_name", last_name);
                    intent.putExtra("date", date_of_birth);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone_number);
                    intent.putExtra("address", office_address);
                    startActivity(intent);

                }
                if (item.getTitle().equals("Change Profile")) {
                    Intent intent = new Intent(DoctorProfileActivity.this, UploadProfileActivity.class);
                    intent.putExtra("image", user_image);
                    startActivity(intent);

                }
                if (item.getTitle().equals("Change Password")) {
                    Intent intent = new Intent(DoctorProfileActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);

                }

                return true;
            }
        });

        menu.show(); //showing popup menu
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadProfile();

    }
}