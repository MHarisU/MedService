package com.med.medservice.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonArrayToList {

    public static List<String> createList(Context context, String jsonArray) {

        List<String> list = null;
        list = new ArrayList<String>();

        JSONArray parent = null;
        try {
            parent = new JSONArray(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //stateNames = new String[parent.length() + 1];

        //stateNames[0] = "Select Your State";
        for (int i = 0; i < parent.length(); i++) {


            JSONObject child = null;
            try {
                child = parent.getJSONObject(i);
                list.add(child.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return list;
    }

}
