package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class appoint_setItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_set_item);
    }

    public void selectFriend(View v) {
        Intent intent = new Intent(appoint_setItem.this, invite.class);
        startActivity(intent);
    }

//    public void selectTheme(View v) {
//        Intent intent = new Intent(appoint_setItem.this, theme.class);
//        startActivity(intent);
//    }
}
