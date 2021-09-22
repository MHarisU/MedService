package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.Models.PanelLabs.PanelLabsAdapter;
import com.med.medservice.Models.PanelLabs.PanelLabsList;
import com.med.medservice.Models.ProductImaging.ImagingList;
import com.med.medservice.NetworkAPI.ApiCallerNew;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Diaglogs.ViewDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ImagingDetailActivity extends AppCompatActivity {


    TextView ProductQuantity, ProductSubTotal;

    ImageView labsImageView;
    TextView labsNameView, labsShortDesc, labsPriceView;

    ImagingList currentData;
    String lab_id;// = currentData.getMedicine_id();
    String lab_name;// = currentData.getMedicine_name();
    String lab_desc;// = currentData.getMedicine_short_desc();
    String lab_price;// = currentData.getMedicine_regular_price();
    String lab_image;

    CartDBHelper mydb;
    String name, user_id, email;
    SessionManager sessionManager;


    TextView cartNumberView;

    LinearLayout panelTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imaging_detail);


        ProductQuantity = findViewById(R.id.productQuantityText);

        mydb = new CartDBHelper(this);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);

        cartNumberView = findViewById(R.id.cartNumberView);


        GetMedicineDetails();

        SetupUI();
    }


    private void GetMedicineDetails() {
        Intent intent = getIntent();
        currentData = (ImagingList) intent.getSerializableExtra("selectedLab");
        if (currentData != null) {
            lab_id = String.valueOf(currentData.getId());
        }
        if (currentData != null) {
            lab_name = currentData.getName();
        }
        if (currentData != null) {
            lab_desc = currentData.getShort_description();
        }
        if (currentData != null) {
            lab_price = currentData.getSale_price();
        }
        if (currentData != null) {
            lab_image = currentData.getFeatured_image();
        }
    }

    private void SetupUI() {

        labsImageView = findViewById(R.id.labsImageView);
        labsNameView = findViewById(R.id.labsNameView);
        labsShortDesc = findViewById(R.id.labsShortDesc);
        labsPriceView = findViewById(R.id.labsPriceView);
        panelTest = findViewById(R.id.panelTest);


        Picasso.get()
                .load(lab_image)
                // .placeholder(context.getResources().getDrawable(R.drawable.ic))
                .into(labsImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // if (holderr.Course_Progress != null) {
                        //      holderr.Course_Progress.setVisibility(View.GONE);
                        //  }
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        //  holder.medicine_image_view.setText(medicine_name.substring(0, 1));

        labsNameView.setText(lab_name);
        FetchPanels();


        labsPriceView.setText("$" + lab_price + ".00");

        if (lab_desc != null && !lab_desc.equals("") && !lab_desc.equals("null")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                labsShortDesc.setText(Html.fromHtml(lab_desc, Html.FROM_HTML_MODE_COMPACT));
            } else {

                labsShortDesc.setText(Html.fromHtml(lab_desc));
            }
        } else labsShortDesc.setText("Description for this product is not available");


    }

    private void FetchPanels() {

        final ArrayList<PanelLabsList> labsList;
        final RecyclerView labsRecycler;

        labsRecycler = findViewById(R.id.labsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        labsRecycler.setLayoutManager(new LinearLayoutManager(this));
        labsList = new ArrayList<PanelLabsList>();

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_lab_panels.php?panel_id=" + lab_id,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            try {


                                try {

                                    JSONArray parent = new JSONArray(response);

                                    String including_test = null;
                                    for (int i = 0; i < parent.length(); i++) {
                                        JSONObject child = parent.getJSONObject(i);
                                        including_test = child.getString("including_test");
                                        Log.d("PanelsApiResponse", including_test);

                                        /*String name = child.getString("name");
                                        String parent_category = child.getString("parent_category");
                                        String sub_category = child.getString("sub_category");

                                        popularMedsList.add(new MedicineList(id, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                                quantity, short_description, description, stock_status));*/

                                    }

                                    JSONArray parent1 = new JSONArray(including_test);

                                    if (parent1.length() > 0) {

                                        panelTest.setVisibility(View.VISIBLE);

                                        for (int i = 0; i < parent1.length(); i++) {
                                            JSONObject child = parent1.getJSONObject(i);
                                            String test_name = child.getString("test_name");
                                            String price = child.getString("price");
                                            String slug = child.getString("slug");
                                            String cpt_code = child.getString("cpt_code");

                                            labsList.add(new PanelLabsList(test_name, price, slug, cpt_code));

                                        }
                                    } else {
                                        panelTest.setVisibility(View.GONE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    panelTest.setVisibility(View.GONE);

                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                        PanelLabsAdapter adapter = new PanelLabsAdapter(labsList, ImagingDetailActivity.this);
                        labsRecycler.setAdapter(adapter);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void Close(View view) {
        finish();
    }

    public void DecreaseQuantity(View view) {

        String temp = ProductQuantity.getText().toString();
        if (!temp.equals("1")) {
            int existing = Integer.parseInt(temp);
            existing = existing - 1;
            temp = String.valueOf(existing);
            ProductQuantity.setText(temp);

            //  int pro_price_int = Integer.parseInt(rupee);
            //  pro_price_int = existing * pro_price_int;
            //  String pro_price_string = String.valueOf(pro_price_int);
            //    ProductSubTotal.setText(pro_price_string);

        }


    }

    public void IncreaseQuantity(View view) {

        String temp = ProductQuantity.getText().toString();
        int existing = Integer.parseInt(temp);
        existing = existing + 1;
        temp = String.valueOf(existing);
        ProductQuantity.setText(temp);

        //  int pro_price_int = Integer.parseInt(rupee);
        // pro_price_int = existing * pro_price_int;
        // String pro_price_string = String.valueOf(pro_price_int);
        //ProductSubTotal.setText(pro_price_string);

    }

    public void AddToCart(View view) {

        mydb.insertCartItem(user_id, lab_id, lab_name, ProductQuantity.getText().toString().trim(), lab_price, "0", "lab-test", lab_image);

        ViewDialog alert = new ViewDialog();
        alert.showDialog(this, "(" + ProductQuantity.getText().toString() + ") " + lab_name + "\nAdded in Cart");

        UpdateCart();

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


    public void UpdateCart() {

        if (mydb.numberOfRows() > 0) {
            cartNumberView.setText("" + mydb.numberOfRows());
        } else {
            cartNumberView.setText("");
        }
    }
}