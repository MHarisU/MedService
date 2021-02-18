package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.med.medservice.Models.OnlineDoctors.OnlineDoctorAdapter;
import com.med.medservice.Models.OnlineDoctors.OnlineDoctorsList;
import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Models.ProductMedicine.MedicineListAdapter;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.GlobalUrlApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EvisitActivity extends AppCompatActivity {


    ArrayList<OnlineDoctorsList> onlineDoctorsLists;
    RecyclerView onlineDoctorsRecycler;

    SwipeRefreshLayout pullToRefresh;

    Timer repeater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_evisit);

        SetupRefreshLayout();

        GetOnlineDoctors();

        StartRepeater();
    }

    private void StartRepeater() {


        /*
        Timer _timer = new Timer();
        _timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // use runOnUiThread(Runnable action)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetOnlineDoctors();

                    }
                });
            }
        }, 5000);

         */


        repeater = new Timer();
        repeater.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              GetOnlineDoctors();
                                          }
                                      });
                                  }

                              },
                5000,
                5000);


    }

    private void SetupRefreshLayout() {
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetOnlineDoctors(); // your code
                //pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void GetOnlineDoctors() {
        //pullToRefresh.setRefreshing(true);

        final ArrayList<OnlineDoctorsList> tempOnlineDoctorsList;
        tempOnlineDoctorsList = new ArrayList<OnlineDoctorsList>();




        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_online_doctors.php",
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            try {

                                //Log.d("ApiResponse", response);

                                try {

                                    JSONArray parent = new JSONArray(response);

                                    for (int i = 0; i < parent.length(); i++) {
                                        JSONObject child = parent.getJSONObject(i);
                                        String id = child.getString("id");
                                        String name = child.getString("name");
                                        String last_name = child.getString("last_name");
                                        String email = child.getString("email");
                                        String specialization = child.getString("specialization");

                                        String full_name = name + " " + last_name;

                                        //onlineDoctorsLists.add(new OnlineDoctorsList(id, full_name, email, specialization));
                                        tempOnlineDoctorsList.add(new OnlineDoctorsList(id, full_name, email, specialization));


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }

                        pullToRefresh.setRefreshing(false);
                        if (tempOnlineDoctorsList != onlineDoctorsLists){

                            onlineDoctorsRecycler = null;
                            onlineDoctorsLists = null;


                            onlineDoctorsRecycler = findViewById(R.id.onlineDoctorsRecycler);
                            // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                            onlineDoctorsRecycler.setLayoutManager(new LinearLayoutManager(EvisitActivity.this));
                            onlineDoctorsLists = tempOnlineDoctorsList;
                            OnlineDoctorAdapter adapter = new OnlineDoctorAdapter(onlineDoctorsLists, EvisitActivity.this);
                            onlineDoctorsRecycler.setAdapter(adapter);

                        }



                        // Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                        //          R.anim.slide_up);
                        ///  cardProgress.setVisibility(View.GONE);
                        //  layoutMain.setVisibility(View.VISIBLE);
                        //  layoutMain.startAnimation(slide_up);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void Close(View view) {
        repeater.cancel();
        finish();
    }

    public void OpenEvisitForm(View view) {
        startActivity(new Intent(EvisitActivity.this, EvisitFormActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        repeater.cancel();
        super.onDestroy();
    }
}