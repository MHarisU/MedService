package com.med.medservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.med.medservice.Models.Category.CategorySquareAdapter;
import com.med.medservice.Models.ProductLabs.LabHorizAdapter;
import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Models.ProductLabs.LabsListAdapter;
import com.med.medservice.Models.ProductMedicine.MedicineAdapter;
import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Models.ProductMedicine.MedicineListAdapter;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.UpdateCartInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LabsActivity extends AppCompatActivity implements UpdateCartInterface {




    ArrayList<CategoryList> categoryList;
    RecyclerView categoryRecycler;
    View ChildView;
    int GetItemPosition;

    ArrayList<LabsList> commonLabsList;
    RecyclerView commonLabsRecycler;

    ArrayList<LabsList> popularLabsList;
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
        setContentView(R.layout.activity_labs);
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


        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_labs_category.php",
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

                        CategorySquareAdapter adapter = new CategorySquareAdapter(categoryList, getApplicationContext());
                        categoryRecycler.setAdapter(adapter);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private void GetRecent() {

        commonLabsRecycler = findViewById(R.id.recentBoughtRecycler);
        commonLabsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false));
        commonLabsList = new ArrayList<LabsList>();


        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "search_lab_by_name.php?q=panel",
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

                                    commonLabsList.add(new LabsList(id, panel_name, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                            quantity, short_description, description, stock_status));


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                        LabHorizAdapter adapter = new LabHorizAdapter(commonLabsList, LabsActivity.this);
                        commonLabsRecycler.setAdapter(adapter);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private void GetPopular() {


        popularLabsRecycler = findViewById(R.id.popularMedsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularLabsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        popularLabsList = new ArrayList<LabsList>();

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
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void openSearch(View view) {
        Intent intent = new Intent(LabsActivity.this, SearchActivity.class);
        intent.putExtra("from", "lab");
        startActivity(intent);
    }


    public void Close(View view) {
        finish();
    }

    public void OpenMedicine(View view) {
        startActivity(new Intent(LabsActivity.this, LabDetailActivity.class));

    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    public void OpenHome(View view) {
        Intent i = new Intent(this, PatientMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }


    @Override
    protected void onResume() {
        super.onResume();
        UpdateCart();
    }

    @Override
    public void UpdateCart() {

        if (mydb.numberOfRows() > 0) {
            cartNumberView.setText("" + mydb.numberOfRows());
        } else {
            cartNumberView.setText("");
        }
    }

    public void OpenLabsCategory(View view) {
        startActivity(new Intent(LabsActivity.this, LabsCategoryActivity.class));
    }
}