package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainAttendent_Reviews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_attend_reviews);
    }
}
