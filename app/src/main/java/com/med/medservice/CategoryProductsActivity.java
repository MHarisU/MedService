package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.Models.Category.CategoryAdapter;
import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Models.ProductMedicine.MedicineListAdapter;
import com.med.medservice.Models.SubCategory.SubCategoryAdapter;
import com.med.medservice.Models.SubCategory.SubCategoryList;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.ApiTokenCaller;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.UpdateCartInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryProductsActivity extends AppCompatActivity implements UpdateCartInterface {

    CategoryList selectedCategory;
    TextView categoryNameView, MedicinesInView;

    ArrayList<SubCategoryList> categoryList;
    RecyclerView subCategoryRecycler;
    View ChildView;
    int GetItemPosition;


    ArrayList<MedicineList> popularMedsList;
    RecyclerView popularMedsRecycler;


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
        setContentView(R.layout.activity_category_products);
        cartNumberView = findViewById(R.id.cartNumberView);
        mydb = new CartDBHelper(this);
        UpdateCart();


        cardProgress = findViewById(R.id.cardProgress);
        layoutMain = findViewById(R.id.layoutMain);

        categoryNameView = findViewById(R.id.categoryNameView);
        MedicinesInView = findViewById(R.id.MedicinesInView);
        Intent intent = getIntent();
        selectedCategory = (CategoryList) intent.getSerializableExtra("selectedCategory");
        categoryNameView.setText(selectedCategory.getCategory_name().toString());
        MedicinesInView.setText(selectedCategory.getCategory_name().toString());

        GetSubCategories();

        GetMedicines();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateCart();
    }

    private void GetMedicines() {


        popularMedsRecycler = findViewById(R.id.medicinesInCategoryRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularMedsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        popularMedsList = new ArrayList<MedicineList>();

        //old api
        /*
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_medicines_by_category.php?cat_id=" + selectedCategory.getCategory_id(),
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


                        MedicineListAdapter adapter = new MedicineListAdapter(popularMedsList, CategoryProductsActivity.this);
                        popularMedsRecycler.setAdapter(adapter);


                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_up);
                        cardProgress.setVisibility(View.GONE);
                        layoutMain.setVisibility(View.VISIBLE);
                        layoutMain.startAnimation(slide_up);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/


        new ApiTokenCaller(CategoryProductsActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getProducts?parent_category="+ selectedCategory.getCategory_id() +"&mode=medicine",
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




                            MedicineListAdapter adapter = new MedicineListAdapter(popularMedsList, CategoryProductsActivity.this);
                            popularMedsRecycler.setAdapter(adapter);


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

    private void GetSubCategories() {

        subCategoryRecycler = findViewById(R.id.subCategoryRecycler);
        subCategoryRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false));
        categoryList = new ArrayList<SubCategoryList>();
        subCategoryRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

                    SubCategoryList selectedCategory = categoryList.get(GetItemPosition);


                    /*
                    try {
                        if (selectedCourse.getLessons_ids().get(0) != null && !selectedCourse.getLessons_ids().get(0).equals("")) {
                            Intent i = new Intent(getActivity(), CourseActivity.class);
                            i.putExtra("selectedCourse", selectedCourse);
                            startActivity(i);

                        } else {
                            Toast.makeText(getActivity(), "Fetching Course Contents Please Wait...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
                        Toast.makeText(getActivity(), "Fetching Course Contents Please Wait...", Toast.LENGTH_SHORT).show();
                    }

                     */
                    // Log.d("CLICKED", selectedCategory.getCategory_name());
                    // Toast.makeText(getApplicationContext(), "" + selectedCategory.getCategory_name(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), SubcategoryProductsActivity.class);
                    i.putExtra("selectedCategory", selectedCategory);
                    startActivity(i);
                    //  startActivity(new Intent(getApplicationContext(), CategoryProductsActivity.class));

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


        //old api
        /*
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_sub_categories.php?cat_id=" + selectedCategory.getCategory_id(),
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {


                        try {
                            Log.d("SUB_CATEGORIES", response);
                            // Log.d("Categories: ", response);
                            try {

                                JSONArray parent = new JSONArray(response);

                                for (int i = 0; i < parent.length(); i++) {
                                    JSONObject child = parent.getJSONObject(i);
                                    String id = child.getString("id");
                                    String title = child.getString("title");
                                    String description = child.getString("description");
                                    String parent_category_id = child.getString("parent_id");
                                    String thumbnail = child.getString("thumbnail");

                                    categoryList.add(new SubCategoryList(id, title, description, parent_category_id, thumbnail));


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                        SubCategoryAdapter adapter = new SubCategoryAdapter(categoryList, CategoryProductsActivity.this);
                        subCategoryRecycler.setAdapter(adapter);


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/


        new ApiTokenCaller(CategoryProductsActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProductSubCategories?parent_id="+ selectedCategory.getCategory_id(),
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
                                String title = child.getString("title");
                                String description = child.getString("description");
                                String parent_category_id = child.getString("parent_id");
                                String thumbnail = child.getString("thumbnail");

                                categoryList.add(new SubCategoryList(id, title, description, parent_category_id, thumbnail));


                            }



                            SubCategoryAdapter adapter = new SubCategoryAdapter(categoryList, CategoryProductsActivity.this);
                            subCategoryRecycler.setAdapter(adapter);


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

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    @Override
    public void UpdateCart() {


        if (mydb.numberOfRows() > 0) {
            cartNumberView.setText("" + mydb.numberOfRows());
        } else {
            cartNumberView.setText("");
        }
    }
}