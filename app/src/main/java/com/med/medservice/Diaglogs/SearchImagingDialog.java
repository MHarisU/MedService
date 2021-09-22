package com.med.medservice.Diaglogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.Models.ProductImaging.ImagingList;
import com.med.medservice.Models.ProductImaging.ImagingSearchAdapter;
import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Models.ProductLabs.LabsSearchAdapter;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchImagingDialog {

    ArrayList<ImagingList> popularLabsList;
    RecyclerView popularMedsRecycler;
    EditText searchEditView;

    int queryCount = 0;

    String from;

    Dialog dialog;
    Context context;

    public SearchImagingDialog() {
    }

    public void showSearchImagingDialog(final Context activity) {
        context = activity;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.search_imaging_dialog_layout);


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

                if (queryCount > 1) {

                    queryCount = 0;
                    if (!query.equals("")) {
                        popularMedsRecycler = null;
                        popularLabsList = null;
                        SearchLabsQuery(new GlobalUrlApi().getNewBaseUrl() + "getSearchProducts?keyword="+query+"&limit=50&mode=imaging");
                    } else {
                        SearchLabsQuery(new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=imaging");

                    }
                } else {
                    queryCount++;
                }

            }
        });

        SearchLabsQuery(new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=imaging");


        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    private void SearchLabsQuery(String SearchURL) {

        popularMedsRecycler = null;
        popularLabsList = null;

        popularMedsRecycler = (RecyclerView) dialog.findViewById(R.id.medicinesSearchRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularMedsRecycler.setLayoutManager(new LinearLayoutManager(context));
        popularLabsList = new ArrayList<ImagingList>();


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

                        LabsSearchAdapter adapter = new LabsSearchAdapter(popularLabsList, context);
                        popularMedsRecycler.setAdapter(adapter);
                    }
                });
        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/


        new ApiTokenCaller(context, SearchURL,
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



                                popularLabsList.add(new ImagingList(Integer.parseInt(id), name, slug, parent_category, sub_category, featured_image, sale_price, regular_price,
                                        quantity, mode, medicine_type, is_featured, short_description, description,
                                        cpt_code, test_details, including_test, stock_status));


                            }


                            ImagingSearchAdapter adapter = new ImagingSearchAdapter(popularLabsList, context);
                            popularMedsRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );
    }

}