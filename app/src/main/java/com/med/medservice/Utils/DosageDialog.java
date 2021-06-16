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
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.DoctorVideoActivity;
import com.med.medservice.Models.SearchPharmacies.PharmaciesAdapter;
import com.med.medservice.Models.SearchPharmacies.PharmaciesList;
import com.med.medservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DosageDialog {

    ArrayList<PharmaciesList> pharmaciesList;
    RecyclerView pharmaciesRecycler;
    EditText searchEditView;

    View ChildView;
    int GetItemPosition;

    int queryCount = 0;

    String from;

    Dialog dialog;
    Context context;

    public DosageDialog() {
    }


    public interface AsyncApiResponse {
        void processFinish(String response);
    }

    public AsyncApiResponse delegate = null;

    public void showDosageDialog(final Context activity, AsyncApiResponse delegate) throws JSONException {
        this.delegate = delegate;
        context = activity;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dosage_dialog_layout);


        ImageView closeButton = (ImageView) dialog.findViewById(R.id.closeButton);

        RadioButton twentyFourHoursRadio = (RadioButton) dialog.findViewById(R.id.twentyFourHoursRadio);
        RadioButton twelveHoursRadio = (RadioButton) dialog.findViewById(R.id.twelveHoursRadio);
        RadioButton eightHoursRadio = (RadioButton) dialog.findViewById(R.id.eightHoursRadio);
        RadioButton sixHoursRadio = (RadioButton) dialog.findViewById(R.id.sixHoursRadio);

        EditText quantityEditView = (EditText) dialog.findViewById(R.id.quantityEditView);
        EditText NumberOfDaysEditView = (EditText) dialog.findViewById(R.id.NumberOfDaysEditView);
        EditText instructionEditView = (EditText) dialog.findViewById(R.id.instructionEditView);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String day="24hrs";

                if (twentyFourHoursRadio.isChecked()) {
                    day = "24hrs";
                } else if (twelveHoursRadio.isChecked()) {
                    day = "12hrs";
                } else if (eightHoursRadio.isChecked()) {
                    day = "8hrs";
                } else if (sixHoursRadio.isChecked()) {
                    day = "6hrs";
                }

                String jsonString = null;
                try {
                    jsonString = new JSONObject()
                            .put("comment", instructionEditView.getText().toString())
                            .put("quantity", quantityEditView.getText().toString())
                            .put("usage", "Dosage: Every "+day+" for "+NumberOfDaysEditView.getText().toString()+" day")
                            .toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(jsonString);
                delegate.processFinish(jsonString);

                dialog.dismiss();
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