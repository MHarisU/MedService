package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.med.medservice.Models.ProductImaging.ImagingList;
import com.med.medservice.Models.ProductImaging.ImagingListAdapter;
import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Models.ProductLabs.LabsListAdapter;
import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Models.ProductMedicine.MedicineListAdapter;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.NetworkAPI.GlobalUrlApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    // View ChildView;
    // int GetItemPosition;

    ArrayList<MedicineList> popularMedsList;
    ArrayList<LabsList> popularLabsList;
    ArrayList<ImagingList> popularImagingList;
    RecyclerView popularMedsRecycler;

    EditText searchEditView;

    int queryCount = 0;

    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);

        searchEditView = findViewById(R.id.searchEditView);

        Intent intent = getIntent();
        from = intent.getStringExtra("from");

        if (from.equals("pharmacy")) {

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

                    if (queryCount > 1) {

                        queryCount = 0;
                        if (!query.equals("")) {
                            popularMedsRecycler = null;
                            popularMedsList = null;
                            SearchPharmacyQuery(new GlobalUrlApi().getNewBaseUrl() + "getSearchProducts?keyword="+query+"&limit=100&mode=medicine");
                        } else {
                            SearchPharmacyQuery(new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=medicine");

                        }
                    } else {
                        queryCount++;
                    }

                }
            });

            SearchPharmacyQuery(new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=medicine");

        }
        else if (from.equals("lab")) {

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

                    if (queryCount > 1) {

                        queryCount = 0;
                        if (!query.equals("")) {
                            popularMedsRecycler = null;
                            popularLabsList = null;
                            SearchLabsQuery(new GlobalUrlApi().getNewBaseUrl() + "getSearchProducts?keyword="+query+"&limit=100&mode=lab-test");
                        } else {
                            SearchLabsQuery(new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=lab-test");

                        }
                    } else {
                        queryCount++;
                    }

                }
            });

            SearchLabsQuery(new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=lab-test");
        }
        else if (from.equals("imaging")) {

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

                    if (queryCount > 1) {

                        queryCount = 0;
                        if (!query.equals("")) {
                            popularMedsRecycler = null;
                            popularLabsList = null;
                            SearchImagingQuery(new GlobalUrlApi().getNewBaseUrl() + "getSearchProducts?keyword="+query+"&mode=imaging");
                        } else {
                            SearchImagingQuery(new GlobalUrlApi().getNewBaseUrl() + "getSearchProducts?keyword=MRI&limit=10&mode=imaging");

                        }
                    } else {
                        queryCount++;
                    }

                }
            });

            SearchImagingQuery(new GlobalUrlApi().getNewBaseUrl() + "getSearchProducts?keyword=MRI&limit=10&mode=imaging");
        }

    }

    private void SearchPharmacyQuery(String SearchURL) {

        popularMedsRecycler = null;
        popularMedsList = null;

        popularMedsRecycler = findViewById(R.id.medicinesSearchRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularMedsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        popularMedsList = new ArrayList<MedicineList>();


        //old api
        /*

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
                                        String id = child.getString("id");
                                        String name = child.getString("name");
                                        String parent_category = child.getString("parent_category");
                                        String sub_category = child.getString("sub_category");
                                        String featured_image = child.getString("featured_image");
                                        String sale_price = child.getString("sale_price");
                                        String regular_price = child.getString("regular_price");
                                        String quantity = child.getString("quantity");
                                        String short_description = child.getString("short_description");
                                        String description = child.getString("description");
                                        String stock_status = child.getString("stock_status");

                                        popularMedsList.add(new MedicineList(id, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                                quantity, short_description, description, stock_status));


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


                        MedicineListAdapter adapter = new MedicineListAdapter(popularMedsList, SearchActivity.this);
                        popularMedsRecycler.setAdapter(adapter);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/



        new ApiTokenCaller(SearchActivity.this, SearchURL,
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


                                String id = child.getString("id");
                                String name = child.getString("name");
                                String parent_category = child.getString("parent_category");
                                String sub_category = child.getString("sub_category");
                                String featured_image = child.getString("featured_image");
                                String sale_price = child.getString("sale_price");
                                String regular_price = child.getString("regular_price");
                                String quantity = child.getString("quantity");
                                String short_description = child.getString("short_description");
                                String description = child.getString("description");
                                String stock_status = child.getString("stock_status");

                                popularMedsList.add(new MedicineList(id, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                        quantity, short_description, description, stock_status));


                            }



                            MedicineListAdapter adapter = new MedicineListAdapter(popularMedsList, SearchActivity.this);
                            popularMedsRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );
    }

    private void SearchLabsQuery(String SearchURL) {

        popularMedsRecycler = null;
        popularLabsList = null;

        popularMedsRecycler = findViewById(R.id.medicinesSearchRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularMedsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        popularLabsList = new ArrayList<LabsList>();


        //old api
        /*

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
                                        String id = child.getString("id");
                                        String name = child.getString("name");
                                        String panel_name = child.getString("panel_name");
                                        String parent_category = child.getString("parent_category");
                                        String sub_category = child.getString("sub_category");
                                        String featured_image = child.getString("featured_image");
                                        String sale_price = child.getString("sale_price");
                                        String regular_price = child.getString("regular_price");
                                        String quantity = child.getString("quantity");
                                        String short_description = child.getString("short_description");
                                        String description = child.getString("description");
                                        String stock_status = child.getString("stock_status");

                                        popularLabsList.add(new LabsList(id, panel_name, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                                quantity, short_description, description, stock_status));
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

                        LabsListAdapter adapter = new LabsListAdapter(popularLabsList, SearchActivity.this);
                        popularMedsRecycler.setAdapter(adapter);
                    }
                });
        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/



        new ApiTokenCaller(SearchActivity.this, SearchURL,
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


                                String id = child.getString("id");
                                String name = child.getString("name");
                                String panel_name = child.getString("panel_name");
                                String parent_category = child.getString("parent_category");
                                String sub_category = child.getString("sub_category");
                                String featured_image = child.getString("featured_image");
                                String sale_price;
                                if (child.getString("sale_price") != null && !child.getString("sale_price").equals("null")) {
                                    sale_price = child.getString("sale_price");
                                }else{
                                    sale_price = "110.00";
                                }
                                String regular_price;
                                if (child.getString("regular_price") != null && !child.getString("regular_price").equals("null")) {
                                    regular_price = child.getString("regular_price");
                                }else{
                                    regular_price = "110.00";
                                }
                                String quantity = child.getString("quantity");
                                String short_description = child.getString("short_description");
                                String description = child.getString("description");
                                String stock_status = child.getString("stock_status");

                                popularLabsList.add(new LabsList(id, panel_name, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                        quantity, short_description, description, stock_status));


                            }


                            LabsListAdapter adapter = new LabsListAdapter(popularLabsList, SearchActivity.this);
                            popularMedsRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );
    }

    private void SearchImagingQuery(String SearchURL) {

        popularMedsRecycler = null;
        popularImagingList = null;

        popularMedsRecycler = findViewById(R.id.medicinesSearchRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularMedsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        popularImagingList = new ArrayList<ImagingList>();


        //old api
        /*

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
                                        String id = child.getString("id");
                                        String name = child.getString("name");
                                        String panel_name = child.getString("panel_name");
                                        String parent_category = child.getString("parent_category");
                                        String sub_category = child.getString("sub_category");
                                        String featured_image = child.getString("featured_image");
                                        String sale_price = child.getString("sale_price");
                                        String regular_price = child.getString("regular_price");
                                        String quantity = child.getString("quantity");
                                        String short_description = child.getString("short_description");
                                        String description = child.getString("description");
                                        String stock_status = child.getString("stock_status");

                                        popularLabsList.add(new LabsList(id, panel_name, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                                quantity, short_description, description, stock_status));
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

                        LabsListAdapter adapter = new LabsListAdapter(popularLabsList, SearchActivity.this);
                        popularMedsRecycler.setAdapter(adapter);
                    }
                });
        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/



        new ApiTokenCaller(SearchActivity.this, SearchURL,
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


                                String id = child.getString("id");
                                String name = child.getString("name");
                                String slug = child.getString("slug");
                                String parent_category = child.getString("parent_category");
                                String sub_category = child.getString("sub_category");
                                String featured_image = child.getString("featured_image");
                                String sale_price = child.getString("sale_price");
                                String regular_price = child.getString("regular_price");
                                String quantity = child.getString("quantity");
                                String mode = child.getString("mode");
                                String medicine_type = child.getString("medicine_type");
                                String is_featured = child.getString("is_featured");
                                String short_description = child.getString("short_description");
                                String description = child.getString("description");
                                String cpt_code = child.getString("cpt_code");
                                String test_details = child.getString("test_details");
                                String including_test = child.getString("including_test");
                                String stock_status = child.getString("stock_status");



                                popularImagingList.add(new ImagingList(Integer.parseInt(id), name, slug, parent_category, sub_category, featured_image, sale_price, regular_price,
                                        quantity, mode, medicine_type, is_featured, short_description, description,
                                        cpt_code, test_details, including_test, stock_status));




                            }


                            ImagingListAdapter adapter = new ImagingListAdapter(popularImagingList, SearchActivity.this);
                            popularMedsRecycler.setAdapter(adapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );
    }


    public void Close(View view) {
        finish();
    }
}