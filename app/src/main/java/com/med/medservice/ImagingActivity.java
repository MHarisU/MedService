package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.Models.Category.CategorySquareAdapter;
import com.med.medservice.Models.ProductImaging.ImagingList;
import com.med.medservice.Models.ProductImaging.ImagingListAdapter;
import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Models.ProductLabs.LabsListAdapter;
import com.med.medservice.Utils.ApiTokenCaller;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.GlobalUrlApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ImagingActivity extends AppCompatActivity {

    ArrayList<CategoryList> categoryList;
    RecyclerView categoryRecycler;
    View ChildView;
    int GetItemPosition;

    ArrayList<ImagingList> popularLabsList;
    RecyclerView popularLabsRecycler;

    CardView cardProgress;
    LinearLayout layoutMain;

    CartDBHelper mydb;

    TextView cartNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imaging);


        cartNumberView = findViewById(R.id.cartNumberView);
        mydb = new CartDBHelper(this);

        UpdateCart();


        cardProgress = findViewById(R.id.cardProgress);
        layoutMain = findViewById(R.id.layoutMain);


        try {

            GetCategories();

            //    GetRecent();

            GetPopular();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void GetPopular() {


        popularLabsRecycler = findViewById(R.id.popularMedsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularLabsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        popularLabsList = new ArrayList<ImagingList>();

        // old
        /*
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_labs.php",
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            try {

                                Log.d("LabssApiResponse", response);

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


                        LabsListAdapter adapter = new LabsListAdapter(popularLabsList, LabsActivity.this);
                        popularLabsRecycler.setAdapter(adapter);


                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_up);
                        cardProgress.setVisibility(View.GONE);
                        layoutMain.setVisibility(View.VISIBLE);
                        layoutMain.startAnimation(slide_up);


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

        new ApiTokenCaller(ImagingActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=imaging&limit=10",
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {
                        //Log.d("token_api_response", response);

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


                            Collections.reverse(popularLabsList);

                            ImagingListAdapter adapter = new ImagingListAdapter(popularLabsList, ImagingActivity.this);
                            popularLabsRecycler.setAdapter(adapter);


                            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_up);
                            cardProgress.setVisibility(View.GONE);
                            layoutMain.setVisibility(View.VISIBLE);
                            layoutMain.startAnimation(slide_up);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );

    }


    private void GetCategories() {
        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false));
        categoryList = new ArrayList<CategoryList>();
        categoryRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(),
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

                    CategoryList selectedCategory = categoryList.get(GetItemPosition);

                    Intent i = new Intent(getApplicationContext(), LabsCategoryActivity.class);
                    i.putExtra("selectedCategory", selectedCategory);
                    startActivity(i);

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


        new ApiTokenCaller(ImagingActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProductParentCategories?category_type=imaging",
                new ApiTokenCaller.AsyncApiResponse() {
                    @Override
                    public void processFinish(String response) {
                        //Log.d("token_api_response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject jsonResponse = jsonObject.getJSONObject("Response");

                            JSONArray arrayData = jsonResponse.getJSONArray("Data");


                            for (int i = 0; i < arrayData.length(); i++) {
                                JSONObject child = arrayData.getJSONObject(i);
                                String id = child.getString("id");
                                String name = child.getString("name");
                                String category_type = child.getString("category_type");
                                String description = child.getString("description");
                                String thumbnail = child.getString("thumbnail");

                                categoryList.add(new CategoryList(id, name, category_type, description, thumbnail));


                            }


                            categoryList.add(new CategoryList("0", "Other", "lab-test", "none", "thumbnail"));


                            CategorySquareAdapter adapter = new CategorySquareAdapter(categoryList, getApplicationContext());
                            categoryRecycler.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        );

    }


    public void UpdateCart() {


        if (mydb.numberOfRows() > 0) {
            cartNumberView.setText("" + mydb.numberOfRows());
        } else {
            cartNumberView.setText("");
        }

    }

    public void Close(View view) {
        finish();
    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    public void openSearch(View view) {
        Intent intent = new Intent(ImagingActivity.this, SearchActivity.class);
        intent.putExtra("from", "pharmacy");
        startActivity(intent);
    }
}