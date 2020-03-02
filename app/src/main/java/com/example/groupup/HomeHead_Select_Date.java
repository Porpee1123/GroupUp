package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HomeHead_Select_Date extends AppCompatActivity {
    String id,eid,nameE,monS,monE,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_calendar);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
    }
}
