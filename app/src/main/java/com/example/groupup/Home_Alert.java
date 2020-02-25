package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home_Alert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
    }
    public void backHome(View v) {
        Intent in = new Intent(this, Home.class);
        startActivity(in);
    }
}
