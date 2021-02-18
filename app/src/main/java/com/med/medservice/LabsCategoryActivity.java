package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Models.ProductLabs.LabsListAdapter;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.UpdateCartInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LabsCategoryActivity extends AppCompatActivity implements UpdateCartInterface {


    ArrayList<LabsList> popularLabsList;
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
        setContentView(R.layout.activity_labs_category);

        Intent intent = getIntent();
        selectedCategory = (CategoryList) intent.getSerializableExtra("selectedCategory");

        cartNumberView = findViewById(R.id.cartNumberView);
        mydb = new CartDBHelper(this);
        UpdateCart();


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

        popularLabsList = new ArrayList<LabsList>();

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

    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));

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

    public void Close(View view) {
        finish();
    }
}