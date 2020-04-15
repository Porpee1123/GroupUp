package com.example.groupup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Head_Place extends AppCompatActivity {

    String uid,eid,nameE,monS,monE,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
Extend_MyHelper.checkInternetLost(this);
        setContentView(R.layout.activity_head__theme);
        uid = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        eid =getIntent().getStringExtra("eid");
        nameE = getIntent().getStringExtra("nameEvent");
        monS = getIntent().getStringExtra("mStart");
        monE = getIntent().getStringExtra("mEnd");
    }
    public void backAppoint(View v) {
        Intent intent = new Intent(Head_Place.this, HomeHead_Appointment.class);
        intent.putExtra("id", uid+"");
        intent.putExtra("email", email+"");
        intent.putExtra("eid",eid+"");
        intent.putExtra("nameEvent",nameE+"");
        intent.putExtra("mStart",monS+"");
        intent.putExtra("mEnd",monE+"");
        intent.putExtra("tab",1+"");
        startActivity(intent);
    }
}
