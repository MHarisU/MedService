package com.med.medservice.Utils;

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

import com.med.medservice.CartActivity;
import com.med.medservice.DoctorVideoActivity;
import com.med.medservice.Models.ProductLabs.LabsSearchAdapter;
import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Models.ProductMedicine.MedicineSearchAdapter;
import com.med.medservice.Models.SearchPharmacies.PharmaciesAdapter;
import com.med.medservice.Models.SearchPharmacies.PharmaciesList;
import com.med.medservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchPharmaciesDialog {

    ArrayList<PharmaciesList> pharmaciesList;
    RecyclerView pharmaciesRecycler;
    EditText searchEditView;

    View ChildView;
    int GetItemPosition;

    int queryCount = 0;

    String from;

    Dialog dialog;
    Context context;

    public SearchPharmaciesDialog() {
    }

    public void showSearchLabsDialog(final Context activity) {
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
                        pharmaciesRecycler = null;
                        pharmaciesList = null;
                        SearchPharmaciesByZip(new GlobalUrlApi().getBaseUrl() + "get_lat_long_by_zip.php?zip=" + query);
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


    private void SearchPharmaciesByZip(String SearchURL) {


        final String[] lat = {""};
        final String[] long_ = {""};

        ApiCallerNew asyncTask = new ApiCallerNew(SearchURL,
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
                                        lat[0] = child.getString("lat");
                                        long_[0] = child.getString("long");

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

                        SearchPharmaciesByLatLong(new GlobalUrlApi().getBaseUrl() + "get_pharmacies_by_zip.php?lat=" + lat[0] + "&long=" + long_[0]);


                    }
                });
        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void SearchPharmaciesByLatLong(String link) {


        pharmaciesRecycler = null;
        pharmaciesList = null;

        pharmaciesRecycler = (RecyclerView) dialog.findViewById(R.id.pharmaciesRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        pharmaciesRecycler.setLayoutManager(new LinearLayoutManager(context));
        pharmaciesList = new ArrayList<PharmaciesList>();
        pharmaciesRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

                    PharmaciesList selectedPharmacy = pharmaciesList.get(GetItemPosition);

                    String selected_Pharmacy_id = selectedPharmacy.getPharmacy_id();
                    String selected_Pharmacy_name = selectedPharmacy.getPharmacy_name();
                    String selected_Pharmacy_address = selectedPharmacy.getPharmacy_address();

                    if (context instanceof DoctorVideoActivity) {
                        ((DoctorVideoActivity) context).setPharmacy(selected_Pharmacy_id, selected_Pharmacy_name, selected_Pharmacy_address);
                        ViewDialog viewDialog = new ViewDialog();
                        dialog.dismiss();
                        viewDialog.showDialog(context, "Pharmacy selected");
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
                                        String id = child.getString("id");
                                        String name = child.getString("name");
                                        String state = child.getString("state");
                                        String address = child.getString("address");
                                        String city = child.getString("city");
                                        String zip_code = child.getString("zip_code");
                                        String marker_type = child.getString("marker_type");
                                        String marker_icon = ""; //child.getString("marker_icon");
                                        String lat = child.getString("lat");
                                        String long_ = child.getString("long");

                                        pharmaciesList.add(new PharmaciesList(id, name, state, address, city, zip_code, marker_type,
                                                marker_icon, lat, long_));


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


                        PharmaciesAdapter adapter = new PharmaciesAdapter(pharmaciesList, context);
                        pharmaciesRecycler.setAdapter(adapter);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

}