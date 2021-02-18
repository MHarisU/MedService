package com.med.medservice.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Models.ProductLabs.LabsListAdapter;
import com.med.medservice.Models.ProductLabs.LabsSearchAdapter;
import com.med.medservice.R;
import com.med.medservice.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchLabDialog {

    ArrayList<LabsList> popularLabsList;
    RecyclerView popularMedsRecycler;
    EditText searchEditView;

    int queryCount = 0;

    String from;

    Dialog dialog;
    Context context;

    public SearchLabDialog() {
    }

    public void showSearchLabsDialog(final Context activity) {
        context = activity;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.search_labs_dialog_layout);


        ImageView closeButton = (ImageView) dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        searchEditView = (EditText) dialog.findViewById(R.id.searchEditView);
        searchEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String query = searchEditView.getText().toString();

                if (queryCount > 1) {

                    queryCount = 0;
                    if (!query.equals("")) {
                        popularMedsRecycler = null;
                        popularLabsList = null;
                        SearchLabsQuery(new GlobalUrlApi().getBaseUrl() + "search_lab_by_name.php?q=" + query);
                    } else {
                        SearchLabsQuery(new GlobalUrlApi().getBaseUrl() + "get_labs.php");

                    }
                } else {
                    queryCount++;
                }

            }
        });

        SearchLabsQuery(new GlobalUrlApi().getBaseUrl() + "get_labs.php");


        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    private void SearchLabsQuery(String SearchURL) {

        popularMedsRecycler = null;
        popularLabsList = null;

        popularMedsRecycler = (RecyclerView) dialog.findViewById(R.id.medicinesSearchRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        popularMedsRecycler.setLayoutManager(new LinearLayoutManager(context));
        popularLabsList = new ArrayList<LabsList>();

        ApiCallerNew asyncTask = new ApiCallerNew(SearchURL,
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

                        LabsSearchAdapter adapter = new LabsSearchAdapter(popularLabsList, context);
                        popularMedsRecycler.setAdapter(adapter);
                    }
                });
        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}