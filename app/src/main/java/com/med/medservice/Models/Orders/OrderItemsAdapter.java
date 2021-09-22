package com.med.medservice.Models.Orders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.R;
import com.med.medservice.NetworkAPI.ApiTokenCaller;
import com.med.medservice.NetworkAPI.GlobalUrlApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ItemsHolder> {


    ArrayList<CartItem> list;
    Context context;
    View view;

    public OrderItemsAdapter() {
    }

    public OrderItemsAdapter(ArrayList<CartItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.order_items_row, parent, false);


        return new ItemsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsHolder holder, int position) {

        final CartItem currentData = list.get(position);

        String product_id = currentData.getProduct_id();
        String product_qty = currentData.getProduct_qty();


        holder.productName.setText(product_id);
        holder.quantityProduct.setText(product_qty);
        
        loadProduct(holder, currentData, product_id);


    }

    private void loadProduct(final ItemsHolder holder, CartItem currentData, String product_id) {


        new ApiTokenCaller(context, new GlobalUrlApi().getNewBaseUrl() +"getProducts?id="+product_id,
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

                                holder.productName.setText(name);

                                if (sale_price != null && !sale_price.equals("null") && !sale_price.equals("")) {
                                    holder.productPrice.setText("$" + sale_price + ".00");

                                } else
                                    holder.productPrice.setText("$" + regular_price + ".00");


                              /*  popularMedsList.add(new MedicineList(id, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                        quantity, short_description, description, stock_status));*/


                            }


/*
                            MedicineListAdapter adapter = new MedicineListAdapter(popularMedsList, SubcategoryProductsActivity.this);
                            popularMedsRecycler.setAdapter(adapter);


                            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_up);
                            cardProgress.setVisibility(View.GONE);
                            layoutMain.setVisibility(View.VISIBLE);
                            layoutMain.startAnimation(slide_up);*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }
        );

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemsHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView quantityProduct;
        TextView productPrice;



        public ItemsHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            quantityProduct = itemView.findViewById(R.id.quantityProduct);
            productPrice = itemView.findViewById(R.id.productPrice);


        }
    }
}
