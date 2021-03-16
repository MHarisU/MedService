package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.med.medservice.Models.Category.CategoryAdapter;
import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.Models.ProductMedicine.MedicineAdapter;
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
import java.util.concurrent.ExecutionException;

public class PharmacyActivity extends AppCompatActivity implements UpdateCartInterface {


    ArrayList<CategoryList> categoryList;
    RecyclerView categoryRecycler;
    View ChildView;
    int GetItemPosition;


    ArrayList<MedicineList> recentMedsList;
    RecyclerView recentMedsRecycler;

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
        setContentView(R.layout.activity_pharmacy);
        cartNumberView = findViewById(R.id.cartNumberView);
        mydb = new CartDBHelper(this);

        UpdateCart();


        cardProgress = findViewById(R.id.cardProgress);
        layoutMain = findViewById(R.id.layoutMain);

        try {

            GetCategories();

            GetRecent();

            GetPopular();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateCart();
    }




    private void GetPopular() {


        popularMedsRecycler = findViewById(R.id.popularMedsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularMedsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        /*
        popularMedsRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

                    MedicineList selectedMed = popularMedsList.get(GetItemPosition);

                    Log.d("CLICKED", selectedMed.getMedicine_name());
                    //  Toast.makeText(getApplicationContext(), "" + selectedCategory.getMedicine_name(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MedDetailActivity.class);
                    intent.putExtra("med_name", selectedMed.getMedicine_name());
                    intent.putExtra("med_image", selectedMed.getMedicine_name());
                    startActivity(new Intent(getApplicationContext(), MedDetailActivity.class));

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
        */

        popularMedsList = new ArrayList<MedicineList>();

        //old api
        /*
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_medicines_rand.php",
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


                        MedicineListAdapter adapter = new MedicineListAdapter(popularMedsList, PharmacyActivity.this);
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



        new ApiTokenCaller(PharmacyActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=medicine",
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



                            MedicineListAdapter adapter = new MedicineListAdapter(popularMedsList, PharmacyActivity.this);
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

    private void GetRecent() {

        recentMedsRecycler = findViewById(R.id.recentBoughtRecycler);
        recentMedsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recentMedsList = new ArrayList<MedicineList>();

        //RecyclerViewClick
        /*
        recentMedsRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

                    MedicineList selectedCategory = recentMedsList.get(GetItemPosition);

                    Log.d("CLICKED", selectedCategory.getMedicine_name());
                    Toast.makeText(getApplicationContext(), "" + selectedCategory.getMedicine_name(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MedDetailActivity.class));


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
         */

        //old api
        /*
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_medicines_rand.php?limit=4",
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
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

                                    recentMedsList.add(new MedicineList(id, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                            quantity, short_description, description, stock_status));


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                        UpdateCartInterface updateCartInterface = null;
                        MedicineAdapter adapter = new MedicineAdapter(recentMedsList, PharmacyActivity.this, updateCartInterface);
                        recentMedsRecycler.setAdapter(adapter);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/


        new ApiTokenCaller(PharmacyActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProducts?mode=medicine",
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

                                recentMedsList.add(new MedicineList(id, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                        quantity, short_description, description, stock_status));


                            }




                            UpdateCartInterface updateCartInterface = null;
                            MedicineAdapter adapter = new MedicineAdapter(recentMedsList, PharmacyActivity.this, updateCartInterface);
                            recentMedsRecycler.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );


    }

    private void GetCategories() {
        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.HORIZONTAL, false));
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
                    Intent i = new Intent(getApplicationContext(), CategoryProductsActivity.class);
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

        //old api calling
        /*
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_product_categories.php",
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {


                        try {
                            // Log.d("ApiResponse", response);
                            // Log.d("Categories: ", response);
                            try {

                                JSONArray parent = new JSONArray(response);

                                for (int i = 0; i < parent.length(); i++) {
                                    JSONObject child = parent.getJSONObject(i);
                                    String id = child.getString("id");
                                    String name = child.getString("name");
                                    String category_type = child.getString("category_type");
                                    String description = child.getString("description");
                                    String thumbnail = child.getString("thumbnail");

                                    categoryList.add(new CategoryList(id, name, category_type, description, thumbnail));


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                        CategoryAdapter adapter = new CategoryAdapter(categoryList, getApplicationContext());
                        categoryRecycler.setAdapter(adapter);


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

        new ApiTokenCaller(PharmacyActivity.this, new GlobalUrlApi().getNewBaseUrl() + "getProductParentCategories?category_type=medicine",
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
                                String category_type = child.getString("category_type");
                                String description = child.getString("description");
                                String thumbnail = child.getString("thumbnail");

                                categoryList.add(new CategoryList(id, name, category_type, description, thumbnail));


                            }



                            CategoryAdapter adapter = new CategoryAdapter(categoryList, getApplicationContext());
                            categoryRecycler.setAdapter(adapter);


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

    public void OpenMedicine(View view) {
        startActivity(new Intent(PharmacyActivity.this, MedDetailActivity.class));

    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    public void openSearch(View view) {
        Intent intent = new Intent(PharmacyActivity.this, SearchActivity.class);
        intent.putExtra("from", "pharmacy");
        startActivity(intent);
    }


    @Override
    public void UpdateCart() {


        if (mydb.numberOfRows() > 0) {
            cartNumberView.setText("" + mydb.numberOfRows());
        }else {
            cartNumberView.setText("");
        }

    }
}