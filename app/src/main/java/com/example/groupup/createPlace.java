package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class createPlace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);
    }
    public void selectThemePlace(View v){
        Intent intent = new Intent(createPlace.this,theme.class);
        startActivity(intent);
    }
}
