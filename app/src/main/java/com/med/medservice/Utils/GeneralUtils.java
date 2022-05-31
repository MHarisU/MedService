package com.med.medservice.Utils;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

public class GeneralUtils {


    public static void selectFamily(Context context, View view) {

        TextView textView = (TextView) view;


        String jsonArray = JsonListToJsonArray.loadJSONFromAsset(context, "at_work.json");
        Log.d("CallBackActivity", jsonArray);
        List<String> list = JsonArrayToList.createList(context, jsonArray);

        PopupMenu menu = LoadListToMenu.loadMenu(context, list, view);


        //registering popup with OnMenuItemClickListener
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                textView.setText(item.getTitle().toString());

                return true;
            }
        });

        menu.show(); //showing popup menu

    }

}
