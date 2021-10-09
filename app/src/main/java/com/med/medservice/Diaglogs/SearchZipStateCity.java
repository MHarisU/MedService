package com.med.medservice.Diaglogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.DoctorRegisterActivity;
import com.med.medservice.Models.ZipCityState.ZipCityAdapter;
import com.med.medservice.Models.ZipCityState.ZipCityList;
import com.med.medservice.NetworkAPI.ApiCallerNew;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.PatientRegisterActivity;
import com.med.medservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchZipStateCity {

    ArrayList<ZipCityList> zipCityStateList;
    RecyclerView cityRecycler;
    EditText searchEditView;

    View ChildView;
    int GetItemPosition;

    int queryCount = 0;

    String from;

    Dialog dialog;
    Context context;


    public SearchZipStateCity() {
    }

    public void showSearchDialog(final Context activity) {

        context = activity;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.search_pharmacies_dialog_layout);


        ImageView closeButton = (ImageView) dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        searchEditView = (EditText) dialog.findViewById(R.id.searchEditView);
        searchEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String query = searchEditView.getText().toString();

                if (query.length() > 4) {

                    queryCount = 0;
                    if (!query.equals("")) {
                        cityRecycler = null;
                        zipCityStateList = null;
                        SearchAddress(new GlobalUrlApi().getBaseUrl() + "get_city_state_by_zipcode.php?zip=" + query);
                    }
                } else {
                    queryCount++;
                }

            }
        });


        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }



    private void SearchAddress(String link) {


        cityRecycler = null;
        zipCityStateList = null;

        cityRecycler = (RecyclerView) dialog.findViewById(R.id.pharmaciesRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        cityRecycler.setLayoutManager(new LinearLayoutManager(context));
        zipCityStateList = new ArrayList<ZipCityList>();
        cityRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent motionEvent) {
                            return true;
                        }
                    });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                ChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    GetItemPosition = recyclerView.getChildAdapterPosition(ChildView);

                    ZipCityList selectedPharmacy = zipCityStateList.get(GetItemPosition);

                    String zip = selectedPharmacy.getZip_code();
                    String city = selectedPharmacy.getCity();
                    String state = selectedPharmacy.getState();
                    String abb = selectedPharmacy.getAbb();

                    if (context instanceof PatientRegisterActivity) {

                        ((PatientRegisterActivity) context).setZipCityState(zip, city, state, abb);
                        ViewDialog viewDialog = new ViewDialog();
                        dialog.dismiss();
                        viewDialog.showDialog(context, "City and State Selected.");
                    }

                    if (context instanceof DoctorRegisterActivity) {

                        ((DoctorRegisterActivity) context).setZipCityState(zip, city, state, abb);
                        ViewDialog viewDialog = new ViewDialog();
                        dialog.dismiss();
                        viewDialog.showDialog(context, "City and State Selected.");
                    }


                    // Toast.makeText(getActivity(), "" + selectedCourse.getLessons_ids().get(0), Toast.LENGTH_SHORT).show();

                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {
            }
        });

        ApiCallerNew asyncTask = new ApiCallerNew(link,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            try {

                                Log.d("ApiResponse", response);

                                try {

                                    JSONArray parent = new JSONArray(response);

                                    for (int i = 0; i < parent.length(); i++) {
                                        JSONObject child = parent.getJSONObject(i);
                                        String zip_code = child.getString("zip_code");
                                        String city = child.getString("city");
                                        String state = child.getString("state");
                                        String abbreviation = child.getString("abbreviation");

                                        zipCityStateList.add(new ZipCityList(zip_code, city, state, abbreviation));


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


                        ZipCityAdapter adapter = new ZipCityAdapter(zipCityStateList, context);
                        cityRecycler.setAdapter(adapter);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

}