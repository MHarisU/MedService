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

import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Models.ProductMedicine.MedicineListAdapter;
import com.med.medservice.Models.SubCategory.SubCategoryList;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.UpdateCartInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubcategoryProductsActivity extends AppCompatActivity implements UpdateCartInterface {

    SubCategoryList selectedCategory;
    TextView categoryNameView, MedicinesInView;

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
        setContentView(R.layout.activity_subcategory_products);
        cartNumberView = findViewById(R.id.cartNumberView);
        mydb = new CartDBHelper(this);
        UpdateCart();


        cardProgress = findViewById(R.id.cardProgress);
        layoutMain = findViewById(R.id.layoutMain);


        categoryNameView = findViewById(R.id.categoryNameView);
        MedicinesInView = findViewById(R.id.MedicinesInView);
        Intent intent = getIntent();
        selectedCategory = (SubCategoryList) intent.getSerializableExtra("selectedCategory");
        categoryNameView.setText(selectedCategory.getSub_category_name().toString());
        MedicinesInView.setText(selectedCategory.getSub_category_name().toString());

        GetMedicines();


    }

    private void GetMedicines() {


        popularMedsRecycler = findViewById(R.id.medicinesInCategoryRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularMedsRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        popularMedsList = new ArrayList<MedicineList>();

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_medicines_by_subcategory.php?sub_cat_id="+selectedCategory.getSub_category_id(),
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


                        MedicineListAdapter adapter = new MedicineListAdapter(popularMedsList, SubcategoryProductsActivity.this);
                        popularMedsRecycler.setAdapter(adapter);


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


    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));

    }

    public void Close(View view) {
        finish();
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
}