package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeHead_Theme extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
    }

    public void backAppoint(View v) {
        Intent intent = new Intent(HomeHead_Theme.this, HomeHead_Appointment.class);
        startActivity(intent);
    }

}
