package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeHead_Appointment_Date_And_Place extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_date_and_place);
    }

    public void selectDateTime(View v) {
        Intent intent = new Intent(HomeHead_Appointment_Date_And_Place.this, InviteFriend.class);
        startActivity(intent);
    }

    public void selectPlace(View v) {
        Intent intent = new Intent(HomeHead_Appointment_Date_And_Place.this, InviteFriend.class);
        startActivity(intent);
    }
}
