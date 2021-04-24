package com.med.medservice.Models.Orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.OrderDetailActivity;
import com.med.medservice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {


    ArrayList<OrderList> list;
    Context context;
    View view;

    public OrderAdapter() {
    }

    public OrderAdapter(ArrayList<OrderList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);


        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {

        final OrderList currentData = list.get(position);

        String order_id = currentData.getOrder_id();
        String order_date = currentData.getCreated_at();


        holder.orderId.setText(order_id);
        holder.orderDate.setText(order_date);

        holder.orderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrder(currentData);
            }
        });

        holder.orderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrder(currentData);
            }
        });

        holder.orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrder(currentData);
            }
        });

    }

    private void openOrder(OrderList currentData) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("orderID", currentData.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class OrderHolder extends RecyclerView.ViewHolder {

        TextView orderId;
        TextView orderDate;
        TextView orderView;



        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderView = itemView.findViewById(R.id.orderView);


        }
    }
}
