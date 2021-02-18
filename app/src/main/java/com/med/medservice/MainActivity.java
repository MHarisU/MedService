package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OpenDrawer(View view) {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme)
                .setTitle("")
                .setMessage("Do you want to close the app?")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNeutralButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        //  homeIntent.addCategory(Intent.CATEGORY_HOME);
                        //  homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  startActivity(homeIntent);
                        finish();
                        System.exit(0);
                    }
                });
        dialog.show();

    }
}