package com.med.medservice.Models.Category;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {


    ArrayList<CategoryList> list;
    Context context;
    View view;

    public CategoryAdapter() {
    }

    public CategoryAdapter(ArrayList<CategoryList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.category_row, parent, false);


        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {

        final CategoryList currentData = list.get(position);

        String category_name = currentData.getCategory_name();
        String category_image = currentData.getCategory_image();

        //   category_name = category_name.replace("&#8211;", "-");


        List<String> colors;

        colors=new ArrayList<String>();

        colors.add("#f2453d");
        colors.add("#fd9727");
        colors.add("#fdc02f");
        colors.add("#50ae54");
        colors.add("#2c98f0");
        colors.add("#4054b2");
        colors.add("#9a30ae");
        colors.add("#e72564");

        Random r = new Random();
        int i1 = r.nextInt(7- 0) + 0;

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.RECTANGLE);
        draw.setColor(Color.parseColor(colors.get(i1)));


        Random rnd = new Random();
        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
     //   holder.category_image_view.setBackgroundColor(currentColor);

        holder.category_image_view.setBackground(draw); //textview
        holder.category_image_view.setText(category_name.substring(0, 1));

        holder.category_name_view.setText(category_name);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CategoryHolder extends RecyclerView.ViewHolder {

        TextView category_name_view;
        TextView category_image_view;


        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            category_name_view = itemView.findViewById(R.id.category_name_view);
            category_image_view = itemView.findViewById(R.id.category_image_view);


        }
    }
}
