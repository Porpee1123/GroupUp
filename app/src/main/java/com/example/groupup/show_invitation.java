package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class show_invitation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_show_invitation);
    }
}
