package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.med.medservice.Models.Orders.Billing;
import com.med.medservice.Models.Orders.CartItem;
import com.med.medservice.Models.Orders.OrderAdapter;
import com.med.medservice.Models.Orders.OrderList;
import com.med.medservice.Models.Orders.Shipping;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {


    ArrayList<OrderList> ordersList;
    RecyclerView orderRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_history);


        getOrders();
    }

    private void getOrders() {
        orderRecycler = findViewById(R.id.ordersRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderRecycler.setLayoutManager(new LinearLayoutManager(this));
        ordersList = new ArrayList<OrderList>();

        new ApiTokenCaller(OrderHistoryActivity.this, new GlobalUrlApi().getNewBaseUrl() +
                "getOrders?customer_id="+ new SessionManager(OrderHistoryActivity.this).getUserId(),
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
                                String order_id = child.getString("order_id");
                                String customer_id = child.getString("customer_id");
                                String total = child.getString("total");

                                JSONObject billingObject = child.getJSONObject("billing");
                                String first_name = billingObject.getString("first_name");
                                String last_name = billingObject.getString("last_name");
                                String address = billingObject.getString("address");
                                String state = billingObject.getString("state");
                                String state_code = billingObject.getString("state_code");
                                String city = billingObject.getString("city");
                                String zip_code = billingObject.getString("zip_code");
                                String phone_number = billingObject.getString("phone_number");
                                String email_address = billingObject.getString("email_address");
                                String pharmacy_zipcode = billingObject.getString("pharmacy_zipcode");
                                String pharmacy_nearby_location = billingObject.getString("pharmacy_nearby_location");
                                String lab_appointment_date = billingObject.getString("lab_appointment_date");
                                String lab_appointment_time = billingObject.getString("lab_appointment_time");
                                String lab_zipcode = billingObject.getString("lab_zipcode");
                                String lab_nearby_location = billingObject.getString("lab_nearby_location");
                                Billing billing = new Billing(first_name,  last_name, address, state , state_code, city,
                                        zip_code, phone_number, email_address, pharmacy_zipcode, pharmacy_nearby_location, lab_appointment_date, lab_appointment_time,
                                        lab_zipcode, lab_nearby_location
                                );

                                JSONObject shippingObject =child.getJSONObject("shipping");
                                String full_name = shippingObject.getString("full_name");

                                //String email_address_shipping = shippingObject.getString("email_address");
                                String email_address_shipping = "email@gmail.com";
                                String phone_number_shipping = shippingObject.getString("phone_number");
                                String address_shipping = shippingObject.getString("address");
                                String state_shipping = shippingObject.getString("state");
                                String state_code_shipping = shippingObject.getString("state_code");
                                String zip_code_shipping = shippingObject.getString("zip_code");
                                Shipping shipping = new Shipping(full_name, email_address_shipping, phone_number_shipping, address_shipping,
                                        state_shipping, state_code_shipping, zip_code_shipping
                                );

                                String payment_title = child.getString("payment_title");
                                String payment_method = child.getString("payment_method");

                                ArrayList<CartItem> cart_items = new ArrayList<>();
                                JSONArray cart_items_array = child.getJSONArray("cart_items");
                                for (int j = 0 ;j<cart_items_array.length();j++){
                                    JSONObject orderObject = cart_items_array.getJSONObject(j);
                                    String product_id = orderObject.getString("product_id");
                                    String product_qty = orderObject.getString("product_qty");
                                    String pres_id = orderObject.getString("pres_id");
                                    String doc_session_id = orderObject.getString("doc_session_id");
                                    String product_mode = orderObject.getString("product_mode");
                                    String item_type = orderObject.getString("item_type");
                                    CartItem item = new CartItem(product_id,product_qty,pres_id,doc_session_id,product_mode,item_type);
                                    cart_items.add(item);

                                }

                                String order_status = child.getString("order_status");
                                String created_at = child.getString("created_at");

                                ordersList.add(new OrderList(id, order_id, customer_id, total, billing, shipping, payment_title,
                                        payment_method, cart_items, order_status, created_at));


                            }




                            OrderAdapter adapter = new OrderAdapter(ordersList, OrderHistoryActivity.this);
                            orderRecycler.setAdapter(adapter);



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