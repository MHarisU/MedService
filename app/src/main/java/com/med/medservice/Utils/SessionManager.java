package com.med.medservice.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.med.medservice.DoctorMainActivity;
import com.med.medservice.LoginActivity;
import com.med.medservice.PatientMainActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String ID = "ID";
    public static final String PHONE = "PHONE";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String id, String fist_name,String last_name, String email, String password,  String user_type, String phone) {
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.putString(FIRST_NAME, fist_name);
        editor.putString(LAST_NAME, last_name);
        editor.putString(USER_TYPE, user_type);
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.putString(PHONE, phone);

        editor.apply();

    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLoginSplash() {
        if (!this.isLogin()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((DoctorMainActivity) context).finish();
        }
    }

    public void checkLogin() {
        if (!this.isLogin()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((DoctorMainActivity) context).finish();
        }
    }

    public void checkLogin(Context context) {
        if (!this.isLogin()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((DoctorMainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {

        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(FIRST_NAME, sharedPreferences.getString(FIRST_NAME, null));
        user.put(LAST_NAME, sharedPreferences.getString(LAST_NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PASSWORD, sharedPreferences.getString(PASSWORD, null));
        user.put(USER_TYPE, sharedPreferences.getString(USER_TYPE, null));
        user.put(PHONE, sharedPreferences.getString(PHONE, null));



        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();

           Intent i = new Intent(context, LoginActivity.class);
           context.startActivity(i);
           ((DoctorMainActivity) context).finish();
    }

    public void logoutPatient() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((PatientMainActivity) context).finish();
    }
}
