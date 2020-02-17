package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class appoint_date_and_place extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_date_and_place);
    }

    public void selectDateTime(View v) {
        Intent intent = new Intent(appoint_date_and_place.this, invite.class);
        startActivity(intent);
    }

    public void selectPlace(View v) {
        Intent intent = new Intent(appoint_date_and_place.this, invite.class);
        startActivity(intent);
    }
}
