package com.med.medservice.Models.PanelLabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.LabDetailActivity;
import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.R;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.GlobalUrlApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PanelLabsAdapter extends RecyclerView.Adapter<PanelLabsAdapter.LabsHolder> {


    ArrayList<PanelLabsList> list;
    Context context;
    View view;
    LabsList lab;

    public PanelLabsAdapter() {
    }

    public PanelLabsAdapter(ArrayList<PanelLabsList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LabsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.panel_lab_row, parent, false);


        return new LabsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabsHolder holder, int position) {

        final PanelLabsList currentData = list.get(position);

        String lab_name = currentData.getLab_name();
        String lab_price = currentData.getLab_price();
        String lab_cpt = currentData.getLab_cpt();


        if (lab_name != null && !lab_name.equals("null") && !lab_name.equals(""))
            holder.labName.setText(lab_name);

        if (lab_price != null && !lab_price.equals("null") && !lab_price.equals(""))
            holder.labPrice.setText(lab_price);

        if (lab_cpt != null && !lab_cpt.equals("null") && !lab_cpt.equals(""))
            holder.labCpt.setText(lab_cpt);


        holder.labName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchLab(currentData.getLab_slug());

            }
        });

        holder.labPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchLab(currentData.getLab_slug());

            }
        });

        holder.labCpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchLab(currentData.getLab_slug());

            }
        });


    }

    private void FetchLab(String lab_slug) {

        final LabsList[] labSelected = new LabsList[1];
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_lab_by_slug.php?slug=" + lab_slug,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            try {


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

                                        labSelected[0] = new LabsList(id, panel_name, name, parent_category, sub_category, featured_image, sale_price, regular_price,
                                                quantity, short_description, description, stock_status);

                                        Intent intent = new Intent(context, LabDetailActivity.class);
                                        intent.putExtra("selectedLab", labSelected[0]);
                                        context.startActivity(intent);

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


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class LabsHolder extends RecyclerView.ViewHolder {

        TextView labName;
        TextView labPrice;
        TextView labCpt;


        public LabsHolder(@NonNull View itemView) {
            super(itemView);

            labName = itemView.findViewById(R.id.labName);
            labPrice = itemView.findViewById(R.id.labPrice);
            labCpt = itemView.findViewById(R.id.labCpt);


        }
    }
}
