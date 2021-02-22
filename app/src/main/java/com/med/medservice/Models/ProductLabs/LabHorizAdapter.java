package com.med.medservice.Models.ProductLabs;

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

import com.med.medservice.LabDetailActivity;
import com.med.medservice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LabHorizAdapter extends RecyclerView.Adapter<LabHorizAdapter.LabsHolder> {


    ArrayList<LabsList> list;
    Context context;
    View view;


    public LabHorizAdapter() {
    }

    public LabHorizAdapter(ArrayList<LabsList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LabsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.category_row, parent, false);


        return new LabsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LabsHolder holder, int position) {

        final LabsList currentData = list.get(position);

        String category_name = currentData.getLab_name();
        String category_image = currentData.getLab_image();

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

        holder.lab_character_view.setBackground(draw); //textview
        holder.lab_character_view.setText(category_name.substring(0, 1));


        holder.lab_name_view.setText(category_name);

        holder.lab_name_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sentence = holder.lab_name_view.getText().toString();
                String search  = "Panel";

                if ( sentence.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {

                    System.out.println("I found the keyword");

                } else {

                    System.out.println("not found");

                }

                Intent intent = new Intent(context, LabDetailActivity.class);
                intent.putExtra("selectedLab", currentData);
                context.startActivity(intent);
            }
        });

        holder.lab_character_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sentence = holder.lab_name_view.getText().toString();
                String search  = "Panel";

                if ( sentence.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {

                    System.out.println("I found the keyword");

                } else {

                    System.out.println("not found");

                }

                Intent intent = new Intent(context, LabDetailActivity.class);
                intent.putExtra("selectedLab", currentData);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class LabsHolder extends RecyclerView.ViewHolder {

        TextView lab_name_view;
        TextView lab_character_view;


        public LabsHolder(@NonNull View itemView) {
            super(itemView);

            lab_name_view = itemView.findViewById(R.id.category_name_view);
            lab_character_view = itemView.findViewById(R.id.category_image_view);


        }
    }
}
