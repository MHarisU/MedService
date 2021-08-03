package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;

public class SubstanceAbuseActivity extends AppCompatActivity {

    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_substance_abuse);

        scrollView = findViewById(R.id.scrollView);
    }

    public void Close(View view) {
        finish();
    }

    public void selfpayClick(View view) {
        View targetView = findViewById(R.id.selfpayLayout);
       // targetView.getParent().requestChildFocus(targetView, targetView);
        scrollView.scrollTo(0, (int) targetView.getY());

    }

    public void firstVisit(View view) {
        View targetView = findViewById(R.id.firstVisitLayout);
       // targetView.getParent().requestChildFocus(targetView, targetView);
        scrollView.scrollTo(0, (int) targetView.getY());

    }

    public void prepareFirstVisit(View view) {
        View targetView = findViewById(R.id.prepareFirstVisitLayout);
     //   targetView.getParent().requestChildFocus(targetView, targetView);
        scrollView.scrollTo(0, (int) targetView.getY());

    }

    public void womenClick(View view) {
        View targetView = findViewById(R.id.womenLayout);
     //   targetView.getParent().requestChildFocus(targetView, targetView);
        scrollView.scrollTo(0, (int) targetView.getY());

    }

    public void generalAdultsClick(View view) {
        View targetView = findViewById(R.id.generalAdultsLayout);
      //  targetView.getParent().requestChildFocus(targetView, targetView);
        scrollView.scrollTo(0, (int) targetView.getY());

    }

    public void otherAdultsClick(View view) {
        View targetView = findViewById(R.id.otherAdultsLayout);
     //   targetView.getParent().requestChildFocus(targetView, targetView);
        scrollView.scrollTo(0, (int) targetView.getY());

    }

    public void childrenClick(View view) {
        /*View targetView = findViewById(R.id.childrenLayout);
        targetView.getParent().requestChildFocus(targetView,targetView);*/

        View insideView = findViewById(R.id.childrenLayout);// find the View that you need to scroll to which is inside this ScrollView
        scrollView.scrollTo(0, (int) insideView.getY());
    }

    public void telemedicineClick(View view) {
        View targetView = findViewById(R.id.telemedicineLayout);
      //  targetView.getParent().requestChildFocus(targetView, targetView);
        scrollView.scrollTo(0, (int) targetView.getY());

    }

    public void adolescentClick(View view) {
        View targetView = findViewById(R.id.adolescentLayout);
     //   targetView.getParent().requestChildFocus(targetView, targetView);
        scrollView.scrollTo(0, (int) targetView.getY());

    }
}