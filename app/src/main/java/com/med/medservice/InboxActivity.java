package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class InboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inbox);

        final ImageView message_options = (ImageView) findViewById(R.id.message_options);
        message_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(InboxActivity.this, message_options);
                //Inflating the Popup using xml file
                //   popup.getMenuInflater()
                //         .inflate(R.menu.popup_menu, popup.getMenu());

                popup.getMenu().add("Delete");
                popup.getMenu().add("Mark as read");
                popup.getMenu().add("Archive");

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(
                                InboxActivity.this,
                                "" + menuItem.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }

                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
    }

    public void Close(View view) {
        finish();
    }



    public void OpenChat(View view) {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
    }
}