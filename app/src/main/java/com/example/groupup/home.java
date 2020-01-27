package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void createGroup(View v){
        Intent intent = new Intent(home.this,createGroup.class);
        startActivity(intent);
    }
    public void search(View v){

    }
    public void menuHamberger(View v){
        Intent intent = new Intent(home.this,register.class);
        startActivity(intent);
    }

}
