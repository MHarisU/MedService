package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.Models.ProductImaging.ImagingList;
import com.med.medservice.Models.ProductImaging.ImagingListAdapter;
import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Models.ProductLabs.LabsListAdapter;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.CartDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImagingCategoryActivity extends AppCompatActivity {


    ArrayList<ImagingList> popularLabsList;
    RecyclerView popularLabsRecycler;

    CardView cardProgress;
    LinearLayout layoutMain;

    CategoryList selectedCategory;

    CartDBHelper mydb;
    TextView cartNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imaging_category);


        Intent intent = getIntent();
        selectedCategory = (CategoryList) intent.getSerializableExtra("selectedCategory");

        cartNumberView = findViewById(R.id.cartNumberView);
        mydb = new CartDBHelper(this);
      //  UpdateCart();


        cardProgress = findViewById(R.id.cardProgress);
        layoutMain = findViewById(R.id.layoutMain);


        try {


            GetPopular();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void GetPopular() {


        popularLabsRecycler = findViewById(R.id.medicinesInCategoryRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularLabsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        popularLabsList = new ArrayList<ImagingList>();

        //old api
        /*

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_lab_by_category.php?cat_id="+selectedCategory.getCategory_id(),
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


                        LabsListAdapter adapter = new LabsListAdapter(popularLabsList, LabsCategoryActivity.this);
                        popularLabsRecycler.setAdapter(adapter);


                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_up);
                        cardProgress.setVisibility(View.GONE);
                        layoutMain.setVisibility(View.VISIBLE);
                        layoutMain.startAnimation(slide_up);


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/


        new ApiTokenCaller(ImagingCategoryActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProducts?parent_category="+selectedCategory.getCategory_id(),
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




                            ImagingListAdapter adapter = new ImagingListAdapter(popularLabsList, ImagingCategoryActivity.this);
                            popularLabsRecycler.setAdapter(adapter);


                            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_up);
                            cardProgress.setVisibility(View.GONE);
                            layoutMain.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );


    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
       // UpdateCart();
    }


    public void Close(View view) {
        finish();
    }
}