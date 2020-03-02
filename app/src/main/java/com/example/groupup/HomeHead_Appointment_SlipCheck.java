package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HomeHead_Appointment_SlipCheck extends AppCompatActivity {
    String id,eid,nameE,monS,monE,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_slip_check);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
    }
}
