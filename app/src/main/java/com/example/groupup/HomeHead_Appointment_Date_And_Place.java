package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeHead_Appointment_Date_And_Place extends AppCompatActivity {
    String id,eid,nameE,monS,monE,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_date_and_place);
        id = getIntent().getStringExtra("id");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
        email = getIntent().getStringExtra("email");
    }

    public void selectDateTime(View v) {
        Intent intent = new Intent(HomeHead_Appointment_Date_And_Place.this, Manage_calendar.class);
        intent.putExtra("id", id+"");
        intent.putExtra("email", email+"");
        intent.putExtra("nEvent", nameE+"");
        intent.putExtra("mStart", monS+"");
        intent.putExtra("mEnd", monE+"");
        intent.putExtra("eid", eid+"");
        startActivity(intent);
    }

    public void selectPlace(View v) {
        Intent intent = new Intent(HomeHead_Appointment_Date_And_Place.this, InviteFriend.class);
        intent.putExtra("id", id+"");
        intent.putExtra("email", email+"");
        intent.putExtra("nEvent", nameE+"");
        intent.putExtra("mStart", monS+"");
        intent.putExtra("mEnd", monE+"");
        intent.putExtra("eid", eid+"");
        startActivity(intent);
    }
}
