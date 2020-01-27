package com.example.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class home extends AppCompatActivity {
    boolean click = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }
    public void setHumburgerButton(){

    }
    public void createGroup(View v){
        Intent intent = new Intent(home.this,createGroup.class);
        startActivity(intent);
    }
    public void search(View v){

    }
    public void menuHamberger(View v){
        FrameLayout fl = (FrameLayout)findViewById(R.id.frameLayout);
        if (click ==false){
            fl.setVisibility(View.VISIBLE);
            fl.bringToFront();
            click=true;
        }else {
            fl.setVisibility(View.INVISIBLE);
            click=false;
        }
    }

}
