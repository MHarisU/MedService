package com.med.medservice.Utils;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

import java.util.List;

public class LoadListToMenu {

    public static PopupMenu loadMenu(Context context, List<String> list, View view) {

        PopupMenu menu = new PopupMenu(context, view);

        for (int i=0; i<list.size(); i++){
            menu.getMenu().add(list.get(i));
        }


        return menu;


    }

}
